package auth.webserver.model.roles;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleView {
    private Role role;
    private List<Permission> permissionList;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }
}
