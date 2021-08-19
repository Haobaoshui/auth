package auth.webserver.model.log;


import auth.webserver.model.roles.Role;
import auth.webserver.model.user.User;
import com.google.gson.Gson;


public class LogRole extends LogBase {
    private User userOperator;
    private Role role;

    public LogRole(int logType, User userOperator, Role role) {
        super(logType);
        this.userOperator = userOperator;
        this.role = role;
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

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        LogRole logRole = gson.fromJson(src, LogRole.class);
        userOperator = logRole.getUserOperator();
        role = logRole.getRole();
    }
}
