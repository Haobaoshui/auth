package auth.webserver.repository.impl;

import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.repository.RoleRepository;
import auth.webserver.utility.GUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String add(Role role) {
        if (role == null) return null;

        return add(role.getName());
    }

    @Override
    public String add(String name) {
        String newid = GUID.getGUID();

        jdbcTemplate.update("INSERT INTO t_role(id,name) VALUES(?,?)", newid, name);
        return newid;
    }

    @Override
    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE from  t_role where id=?", id);
    }

    @Override
    public int delete(Role role) {
        if (role == null || role.getId() == null) return 0;
        return deleteById(role.getId());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("truncate t_role");
    }

    @Override
    public int update(Role role) {
        if (role == null || role.getId() == null) return 0;
        return update(role.getId(), role.getName());
    }

    @Override
    public int update(String id, String name) {
        if (id == null) return 0;

        return jdbcTemplate.update("update t_role set name=? WHERE id=?", name, id);
    }

    @Override
    public boolean isExistById(String id) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_role WHERE id=?", new
                Object[]{id}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public boolean isExistByName(String name) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_role WHERE name=?", new
                Object[]{name}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from t_role", Integer.class);
    }

    @Override
    public int getLikeNameCount(String name) {
        if (name == null || name.length() == 0) return getCount();
        name = "%" + name.trim() + "%";
        return jdbcTemplate.queryForObject("select count(*) from t_role where name like ?", new
                Object[]{name}, Integer.class);
    }

    @Override
    public Role getById(String id) {
        if (!isExistById(id)) return null;

        return jdbcTemplate.queryForObject("SELECT * FROM t_role WHERE id=?", new Object[]{id}, new RoleMapper());
    }

    @Override
    public List<Role> getByLikeName(String name) {
        if (name == null || name.length() == 0) return getAll();

        if (getLikeNameCount(name) == 0) return null;

        name = "%" + name.trim() + "%";

        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_role WHERE name like ?", new
                Object[]{name}, Integer.class) == 0) return null;

        return jdbcTemplate.query("select * from t_role where name like ?", new Object[]{name}, new RoleMapper());
    }

    @Override
    public List<Role> getAll() {
        if (getCount() <= 0) return null;
        return jdbcTemplate.query("select * from t_role",
                new RoleMapper());
    }

    private List<Role> PageQuery(int PageBegin, int PageSize) {
        return jdbcTemplate.query("select * from t_role  limit ?,? ",
                new Object[]{PageBegin * PageSize, PageSize},
                new RoleMapper());
    }

    @Override
    public Page<Role> getPage(int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount();
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<Role> data = PageQuery(pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private List<Role> PageQuery(String searchText, int PageBegin, int PageSize) {
        searchText = "%" + searchText.trim() + "%";
        return jdbcTemplate.query("select * from t_role where name like ? limit ?,? ",
                new Object[]{searchText, PageBegin * PageSize, PageSize},
                new RoleMapper());
    }

    @Override
    public Page<Role> getPage(String searchText, Integer pageNo, Integer pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getLikeNameCount(searchText);
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<Role> data = PageQuery(searchText, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private static final class RoleMapper implements RowMapper<Role> {

        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getString("id"));
            role.setName(rs.getString("name"));

            return role;
        }
    }
}
