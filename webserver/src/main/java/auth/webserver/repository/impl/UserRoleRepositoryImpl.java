package auth.webserver.repository.impl;

import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.user.User;
import auth.webserver.utility.GUID;
import auth.webserver.model.user.UserRole;
import auth.webserver.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRoleRepositoryImpl implements UserRoleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String add(UserRole userRole) {
        if (userRole == null) return null;

        return add(userRole.getUserId(), userRole.getRoleId());
    }

    @Override
    public String add(String userId, String roleId) {
        String newid = GUID.getGUID();

        jdbcTemplate.update("INSERT INTO t_user_role(id,t_user_id,t_role_id) VALUES(?,?,?)", newid, userId, roleId);
        return newid;
    }

    @Override
    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE from  t_user_role where id=?", id);
    }

    @Override
    public int delete(String userId, String roleId) {
        return jdbcTemplate.update("DELETE from  t_user_role where id=?", userId, roleId);
    }

    @Override
    public int delete(UserRole userRole) {
        if (userRole == null || userRole.getId() == null) return 0;
        return deleteById(userRole.getId());
    }

    @Override
    public int delete(User user) {
        if (user == null || user.getId() == null) return 0;

        return jdbcTemplate.update("DELETE from  t_user_role where t_user_id=?", user.getId());
    }

    @Override
    public int delete(Role role) {
        if (role == null || role.getId() == null) return 0;

        return jdbcTemplate.update("DELETE from  t_user_role where t_role_id=?", role.getId());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("truncate t_user_role");
    }

    @Override
    public int update(UserRole userRole) {
        if (userRole == null || userRole.getId() == null) return 0;
        return update(userRole.getId(), userRole.getUserId(), userRole.getRoleId());
    }

    @Override
    public int update(String id, String userId, String roleId) {
        if (id == null || userId == null || roleId == null) return 0;


        return jdbcTemplate.update("update t_user_role set t_user_id=?,t_role_id=? WHERE id=?", userId, roleId, id);
    }

    @Override
    public boolean isExistById(String id) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_user_role WHERE id=?", new
                Object[]{id}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public boolean isExist(String userId, String roleId) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_user_role WHERE t_user_id=? and t_role_id=?", new
                Object[]{userId, roleId}, Integer.class) == 0) return false;
        return true;
    }

    /**
     * 用户是否具有权限
     *
     * @param userId
     * @param permissionId
     * @return
     */
    @Override
    public boolean isExistPermission(String userId, String permissionId) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_user_role \n" +
                "left join t_role on t_user_role.t_role_id=t_role.id \n" +
                "LEFT JOIN t_role_permission ON t_role_permission.t_role_id=t_user_role.t_role_id\n" +
                "WHERE t_user_id=? and t_permission_id=?", new
                Object[]{userId, permissionId}, Integer.class) == 0) return false;
        return true;
    }

    @Override
//    public int getCount() {
//        return jdbcTemplate.queryForObject("select count(*) from t_user_role", Integer.class);
//    }
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(DISTINCT(t_user_id)) from t_user_role", Integer.class);
    }

    @Override
    public int getCountByUserId(String userId) {
        return jdbcTemplate.queryForObject("select count(*) from t_user_role where  t_user_id=?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getCountByRoleId(String roleId) {
        return jdbcTemplate.queryForObject("select count(*) from t_user_role where  t_role_id=?", new Object[]{roleId}, Integer.class);
    }

    @Override
    public UserRole getById(String id) {
        if (!isExistById(id)) return null;

        return jdbcTemplate.queryForObject("SELECT * FROM t_user_role WHERE id=?", new Object[]{id}, new UserRoleMapper());
    }

    @Override
    public UserRole getByUserIdAndRoleId(String userId, String roleId) {

        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_user_role WHERE t_user_id=? and t_role_id=?", new
                Object[]{userId, roleId}, Integer.class) != 1) return null;


        return jdbcTemplate.queryForObject("SELECT * FROM t_user_role WHERE t_user_id=? and t_role_id=?", new Object[]{userId, roleId}, new UserRoleMapper());
    }

    @Override
    public List<UserRole> getAll() {
        if (getCount() <= 0) return null;
        return jdbcTemplate.query("select * from t_user_role",
                new UserRoleMapper());
    }

    @Override
    public List<UserRole> getByUserId(String userId) {
        if (getCountByUserId(userId) <= 0) return null;
        return jdbcTemplate.query("select * from t_user_role where t_user_id=?", new Object[]{userId},
                new UserRoleMapper());
    }

    @Override
    public List<UserRole> getByRoleId(String roleId) {
        if (getCountByRoleId(roleId) <= 0) return null;
        return jdbcTemplate.query("select * from t_user_role where t_role_id=?", new Object[]{roleId},
                new UserRoleMapper());
    }

    @Override
    public List<String> getAllUserIds() {
        if (getCount() <= 0) return null;
        return jdbcTemplate.queryForList("select distinct t_user_id from t_user_role ", String.class);

    }

    private List<UserRole> PageQuery(int PageBegin, int PageSize) {
        return jdbcTemplate.query("select * from t_user_role  limit ?,? ",
                new Object[]{PageBegin * PageSize, PageSize},
                new UserRoleMapper());
    }

    @Override
    public Page<UserRole> getPage(int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount();
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<UserRole> data = PageQuery(pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private List<String> userIdsPageQuery(int PageBegin, int PageSize) {
        return jdbcTemplate.queryForList("select distinct t_user_id from t_user_role  limit ?,? ",
                new Object[]{PageBegin * PageSize, PageSize},
                String.class);
    }

    @Override
    public Page<String> getUserIdsPage(int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount();
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<String> data = userIdsPageQuery(pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }


    private List<String> userIdsPageQuery(String searchText, int PageBegin, int PageSize) {
        return jdbcTemplate.queryForList("select distinct t_user_id from t_user_role left join t_user on t_user.id=t_user_role.t_user_id where (t_user.empl_num like ? or t_user.empl_name like ?) limit ?,? ",
                new Object[]{searchText, searchText, PageBegin * PageSize, PageSize},
                String.class);
    }


    @Override
    public Page<String> getUserIdsPage(String searchText, Integer pageNo, Integer pageSize) {
        long totalCount = jdbcTemplate.queryForObject("select count(distinct t_user_id) from t_user_role  left join t_user on t_user.id=t_user_role.t_user_id where (t_user.empl_num like ? or t_user.empl_name like ?)", new Object[]{searchText, searchText}, Integer.class);
        if (totalCount < 1) return new Page<>();

        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<String> data = userIdsPageQuery(searchText, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private List<String> userIdsPageQueryByRoleId(String t_role_id, int PageBegin, int PageSize) {
        return jdbcTemplate.queryForList("select distinct t_user_id from t_user_role  where t_role_id=? limit ?,? ",
                new Object[]{t_role_id, PageBegin * PageSize, PageSize},
                String.class);
    }

    @Override
    public Page<String> getUserIdsPageByRoleId(String t_role_id, int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = jdbcTemplate.queryForObject("select count(distinct t_user_id) from t_user_role  where t_role_id=?", new Object[]{t_role_id}, Integer.class);
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<String> data = userIdsPageQueryByRoleId(t_role_id, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }


    private List<String> userIdsPageQueryByRoleId(String searchText, String t_role_id, int PageBegin, int PageSize) {
        searchText = "%" + searchText.trim() + "%";
        return jdbcTemplate.queryForList("select distinct t_user_id from t_user_role  left join t_user on t_user.id=t_user_role.t_user_id where (t_user.empl_num like ? or t_user.empl_name like ?) and t_role_id=? limit ?,? ",
                new Object[]{searchText, searchText, t_role_id, PageBegin * PageSize, PageSize},
                String.class);
    }

    @Override
    public Page<String> getUserIdsPageByRoleId(String searchText, String t_role_id, int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = jdbcTemplate.queryForObject("select count(distinct t_user_id) from t_user_role  left join t_user on t_user.id=t_user_role.t_user_id where (t_user.empl_num like ? or t_user.empl_name like ?) and t_role_id=?", new Object[]{searchText, searchText, t_role_id}, Integer.class);
        if (totalCount < 1) return new Page<>();




        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<String> data = userIdsPageQueryByRoleId(searchText, t_role_id, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }


    private static final class UserRoleMapper implements RowMapper<UserRole> {

        @Override
        public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRole userRole = new UserRole();
            userRole.setId(rs.getString("id"));
            userRole.setRoleId(rs.getString("t_role_id"));
            userRole.setUserId(rs.getString("t_user_id"));


            return userRole;
        }
    }
}
