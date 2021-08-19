package auth.webserver.model.log;

import org.springframework.stereotype.Component;

@Component
public class LogsStatistics {
    private String typeName;
    private int value;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
