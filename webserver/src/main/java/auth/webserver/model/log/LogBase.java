package auth.webserver.model.log;

public class LogBase {
    int logType;

    public LogBase(int logType) {
        this.logType = logType;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }
}
