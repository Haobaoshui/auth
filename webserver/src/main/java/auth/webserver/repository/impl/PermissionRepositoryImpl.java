package auth.webserver.repository.impl;

import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;
import auth.webserver.repository.PermissionRepository;
import auth.webserver.utility.GUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PermissionRepositoryImpl implements PermissionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PermissionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String add(Permission permission) {
        if (permission == null) return null;

        return add(permission.getName());
    }

    @Override
    public String add(String name) {
        String newid = GUID.getGUID();

        jdbcTemplate.update("INSERT INTO t_permission(id,name) VALUES(?,?)", newid, name);
        return newid;
    }

    @Override
    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE from  t_permission where id=?", id);
    }

    @Override
    public int delete(Permission permission) {
        if (permission == null || permission.getId() == null) return 0;
        return deleteById(permission.getId());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("truncate t_permission");
    }

    @Override
    public int update(Permission permission) {
        if (permission == null || permission.getId() == null) return 0;
        return update(permission.getId(), permission.getName());
    }

    @Override
    public int update(String id, String name) {
        if (id == null) return 0;

        return jdbcTemplate.update("update t_permission set name=? WHERE id=?", name, id);
    }

    @Override
    public boolean isExistById(String id) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_permission WHERE id=?", new
                Object[]{id}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public boolean isExistByName(String name) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_permission WHERE name=?", new
                Object[]{name}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from t_permission", Integer.class);
    }

    @Override
    public int getLikeNameCount(String name) {
        if (name == null || name.length() == 0) return getCount();
        name = "%" + name.trim() + "%";
        return jdbcTemplate.queryForObject("select count(*) from t_permission where name like ?", new
                Object[]{name}, Integer.class);
    }

    @Override
    public Permission getById(String id) {
        if (!isExistById(id)) return null;

        return jdbcTemplate.queryForObject("SELECT * FROM t_permission WHERE id=?", new Object[]{id}, new PermissionMapper());
    }

    @Override
    public List<Permission> getByLikeName(String name) {
        if (name == null || name.length() == 0) return getAll();

        if (getLikeNameCount(name) == 0) return null;

        name = "%" + name.trim() + "%";

        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_permission WHERE name like ?", new
                Object[]{name}, Integer.class) == 0) return null;

        return jdbcTemplate.query("select * from t_permission where name like ?", new Object[]{name}, new PermissionMapper());
    }

    @Override
    public List<Permission> getAll() {
        if (getCount() <= 0) return null;
        return jdbcTemplate.query("select * from t_permission order by id asc",
                new PermissionMapper());
    }

    private List<Permission> PageQuery(int PageBegin, int PageSize) {
        return jdbcTemplate.query("select * from t_permission  limit ?,? ",
                new Object[]{PageBegin * PageSize, PageSize},
                new PermissionMapper());
    }

    @Override
    public Page<Permission> getPage(int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount();
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<Permission> data = PageQuery(pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private static final class PermissionMapper implements RowMapper<Permission> {

        @Override
        public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
            Permission permission = new Permission();
            permission.setId(rs.getString("id"));
            permission.setName(rs.getString("name"));


            return permission;
        }
    }
}
