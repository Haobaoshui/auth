package auth.webserver.model.log;


import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;
import auth.webserver.model.user.User;
import com.google.gson.Gson;


public class LogRolePermission extends LogBase {
    private User userOperator;
    private Role role;
    private Permission permission;
    private RolePermission rolePermission;

    public LogRolePermission(int logType, User userOperator, Role role, Permission permission, RolePermission rolePermission) {
        super(logType);
        this.userOperator = userOperator;
        this.role = role;
        this.permission = permission;
        this.rolePermission = rolePermission;
    }

    public User getUserOperator() {
        return userOperator;
    }

    public void setUserOperator(User userOperator) {
        this.userOperator = userOperator;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    private RolePermission getRolePermission() {
        return rolePermission;
    }

    public void setRolePermission(RolePermission rolePermission) {
        this.rolePermission = rolePermission;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        LogRolePermission logRolePermission = gson.fromJson(src, LogRolePermission.class);
        userOperator = logRolePermission.getUserOperator();
        role = logRolePermission.getRole();
        permission = logRolePermission.getPermission();
        rolePermission = logRolePermission.getRolePermission();
    }
}
