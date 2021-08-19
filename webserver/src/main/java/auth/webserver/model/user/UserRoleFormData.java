package auth.webserver.model.user;

import auth.webserver.model.roles.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRoleFormData {
    private User user;
    private List<Role> roleList;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
