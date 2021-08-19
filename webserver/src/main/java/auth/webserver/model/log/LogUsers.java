package auth.webserver.model.log;

import auth.webserver.model.user.User;
import com.google.gson.Gson;


public class LogUsers extends LogBase {
    private User userOperator;
    private User user;

    public LogUsers(int logType, User userOperator, User user) {
        super(logType);
        this.userOperator = userOperator;
        this.user = user;
    }

    public User getUserOperator() {
        return userOperator;
    }

    public void setUserOperator(User userOperator) {
        this.userOperator = userOperator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        LogUsers users = gson.fromJson(src, LogUsers.class);
        userOperator = users.getUserOperator();
        user = users.getUser();
    }
}
