package auth.webserver.model.log;


import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;
import auth.webserver.model.user.User;
import com.google.gson.Gson;

import java.util.List;

public class LogRolePermissions extends LogBase {
    private User userOperator;
    private List<Permission> permissionList;
    private Role role;
    private List<RolePermission> rolePermissionList;

    public LogRolePermissions(int logType, User userOperator, Role role, List<Permission> permissionList, List<RolePermission> rolePermissionList) {
        super(logType);
        this.userOperator = userOperator;
        this.permissionList = permissionList;
        this.role = role;
        this.rolePermissionList = rolePermissionList;
    }

    public User getUserOperator() {
        return userOperator;
    }

    public void setUserOperator(User userOperator) {
        this.userOperator = userOperator;
    }

    private List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private List<RolePermission> getRolePermissionList() {
        return rolePermissionList;
    }

    public void setRolePermissionList(List<RolePermission> rolePermissionList) {
        this.rolePermissionList = rolePermissionList;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        LogRolePermissions logRolePermissions = gson.fromJson(src, LogRolePermissions.class);
        userOperator = logRolePermissions.getUserOperator();
        permissionList = logRolePermissions.getPermissionList();
        role = logRolePermissions.getRole();
        rolePermissionList = logRolePermissions.getRolePermissionList();

    }
}
