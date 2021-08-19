package auth.webserver.model.log;


import auth.webserver.model.roles.Permission;
import auth.webserver.model.user.User;
import com.google.gson.Gson;


public class LogPermission extends LogBase {
    private User userOperator;
    private Permission permission;

    public LogPermission(int logType, User userOperator, Permission permission) {
        super(logType);
        this.userOperator = userOperator;
        this.permission = permission;
    }

    public User getUserOperator() {
        return userOperator;
    }

    public void setUserOperator(User userOperator) {
        this.userOperator = userOperator;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        LogPermission logPermission = gson.fromJson(src, LogPermission.class);
        userOperator = logPermission.getUserOperator();
        permission = logPermission.getPermission();
    }
}
