package auth.webserver.repository.impl;

import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;
import auth.webserver.repository.RolePermissionRepository;
import auth.webserver.utility.GUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RolePermissionRepositoryImpl implements RolePermissionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RolePermissionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String add(RolePermission rolePermission) {
        if (rolePermission == null) return null;

        return add(rolePermission.getRoleId(), rolePermission.getPermissionId());
    }

    @Override
    public String add(String roleId, String permissionId) {
        String newid = GUID.getGUID();

        jdbcTemplate.update("INSERT INTO t_role_permission(id,t_role_id,t_permission_id) VALUES(?,?,?)", newid, roleId, permissionId);
        return newid;
    }

    @Override
    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE from  t_role_permission where id=?", id);
    }

    @Override
    public int delete(String roleId, String permissionId) {
        return jdbcTemplate.update("DELETE from  t_role_permission where id=?", roleId, permissionId);
    }

    @Override
    public int delete(RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getId() == null) return 0;
        return deleteById(rolePermission.getId());
    }

    @Override
    public int delete(Role role) {
        if (role == null || role.getId() == null) return 0;
        return jdbcTemplate.update("DELETE from  t_role_permission where t_role_id=?", role.getId());
    }

    @Override
    public int delete(Permission permission) {
        if (permission == null || permission.getId() == null) return 0;
        return jdbcTemplate.update("DELETE from  t_role_permission where t_permission_id=?", permission.getId());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("truncate t_role_permission");
    }

    @Override
    public int update(RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getId() == null) return 0;
        return update(rolePermission.getId(), rolePermission.getRoleId(), rolePermission.getPermissionId());
    }

    @Override
    public int update(String id, String roleId, String permissionId) {
        if (id == null) return 0;

        return jdbcTemplate.update("update t_role_permission set t_role_id=?,note=? t_permission_id id=?", roleId, permissionId, id);
    }

    @Override
    public boolean isExistById(String id) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_role_permission WHERE id=?", new
                Object[]{id}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public boolean isExist(String roleId, String permissionId) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_role_permission WHERE t_role_id=? and t_permission_id=?", new
                Object[]{roleId, permissionId}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from t_role_permission", Integer.class);
    }

    @Override
    public int getCountByPermissionId(String permissionId) {
        return jdbcTemplate.queryForObject("select count(*) from t_role_permission where  t_permission_id=?", new Object[]{permissionId}, Integer.class);
    }

    @Override
    public int getCountByRoleId(String roleId) {
        return jdbcTemplate.queryForObject("select count(*) from t_role_permission where  t_role_id=?", new Object[]{roleId}, Integer.class);
    }

    @Override
    public RolePermission getById(String id) {
        if (!isExistById(id)) return null;

        return jdbcTemplate.queryForObject("SELECT * FROM t_role_permission WHERE id=?", new Object[]{id}, new RolePermissionMapper());
    }

    @Override
    public List<RolePermission> getAll() {
        if (getCount() <= 0) return null;
        return jdbcTemplate.query("select * from t_role_permission",
                new RolePermissionMapper());
    }

    @Override
    public List<RolePermission> getByPermissionId(String permissionId) {
        if (getCountByPermissionId(permissionId) <= 0) return null;
        return jdbcTemplate.query("select * from t_role_permission where t_permission_id=?", new Object[]{permissionId},
                new RolePermissionMapper());
    }

    @Override
    public List<RolePermission> getByRoleId(String roleId) {
        if (getCountByRoleId(roleId) <= 0) return null;
        return jdbcTemplate.query("select * from t_role_permission where t_role_id=?", new Object[]{roleId},
                new RolePermissionMapper());
    }

    private List<RolePermission> PageQuery(int PageBegin, int PageSize) {
        return jdbcTemplate.query("select * from t_role_permission order by name asc limit ?,? ",
                new Object[]{PageBegin * PageSize, PageSize},
                new RolePermissionMapper());
    }

    @Override
    public Page<RolePermission> getPage(int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount();
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<RolePermission> data = PageQuery(pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private static final class RolePermissionMapper implements RowMapper<RolePermission> {

        @Override
        public RolePermission mapRow(ResultSet rs, int rowNum) throws SQLException {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(rs.getString("id"));
            rolePermission.setPermissionId(rs.getString("t_permission_id"));
            rolePermission.setRoleId(rs.getString("t_role_id"));

            return rolePermission;
        }
    }
}
