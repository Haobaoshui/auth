package auth.webserver.repository;



import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;

import java.util.List;

public interface RolePermissionRepository {
    String add(RolePermission rolePermission);

    String add(String roleId, String permissionId);

    int deleteById(String id);

    int delete(String roleId, String permissionId);

    int delete(RolePermission rolePermission);

    int delete(Role role);

    int delete(Permission permission);

    int deleteAll();

    int update(RolePermission rolePermission);

    int update(String id, String roleId, String permissionId);


    boolean isExistById(String id);

    boolean isExist(String roleId, String permissionId);

    int getCount();

    int getCountByPermissionId(String permissionId);

    int getCountByRoleId(String roleId);

    RolePermission getById(String id);


    List<RolePermission> getAll();

    List<RolePermission> getByPermissionId(String permissionId);

    List<RolePermission> getByRoleId(String roleId);

    Page<RolePermission> getPage(int pageNo, int pageSize);
}
