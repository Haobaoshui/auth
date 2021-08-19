package auth.webserver.model.user;

import org.springframework.stereotype.Component;

@Component
public class UserPermissionFormData {
    private User user;
    private int type;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
