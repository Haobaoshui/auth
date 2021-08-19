package auth.webserver.model.log;


import auth.webserver.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class LogsView {
    private Logs logs;
    private User userOperator;
    private String description;//操作概要描述

    public Logs getLogs() {
        return logs;
    }

    public void setLogs(Logs logs) {
        this.logs = logs;
    }

    public User getUserOperator() {
        return userOperator;
    }

    public void setUserOperator(User userOperator) {
        this.userOperator = userOperator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
