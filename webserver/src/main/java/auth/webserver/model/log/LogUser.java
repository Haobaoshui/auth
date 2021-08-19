package auth.webserver.model.log;


import auth.webserver.model.user.User;
import com.google.gson.Gson;

public class LogUser extends LogBase {
    private User user;

    public LogUser(int logType, User user) {
        super(logType);
        this.user = user;
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
        LogUser logUser = gson.fromJson(src, LogUser.class);
        user = logUser.getUser();

    }
}
