package auth.webserver.model.user;


import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class User {
    private String id;
    private String emplNum;
    private String emplName;
    private String emplPwd;

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        User user = gson.fromJson(src, User.class);
        id = user.getId();
        emplName = user.getEmplName();
        emplNum = user.getEmplNum();
        emplPwd = user.getEmplPwd();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmplNum() {
        return emplNum;
    }

    public void setEmplNum(String emplNum) {
        this.emplNum = emplNum;
    }

    public String getEmplName() {
        return emplName;
    }

    public void setEmplName(String emplName) {
        this.emplName = emplName;
    }

    public String getEmplPwd() {
        return emplPwd;
    }

    public void setEmplPwd(String emplPwd) {
        this.emplPwd = emplPwd;
    }
}
