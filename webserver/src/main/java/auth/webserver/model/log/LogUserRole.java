package auth.webserver.model.log;


import auth.webserver.model.roles.Role;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserRole;
import com.google.gson.Gson;


public class LogUserRole extends LogBase {
    private User userOperator;
    private Role role;
    private User user;
    private UserRole userRole;

    public LogUserRole(int logType, User userOperator, User user, Role role, UserRole userRole) {
        super(logType);
        this.userOperator = userOperator;
        this.role = role;
        this.user = user;
        this.userRole = userRole;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        LogUserRole logUserRole = gson.fromJson(src, LogUserRole.class);
        userOperator = logUserRole.getUserOperator();
        role = logUserRole.getRole();
        user = logUserRole.getUser();
        userRole = logUserRole.getUserRole();

    }
}
