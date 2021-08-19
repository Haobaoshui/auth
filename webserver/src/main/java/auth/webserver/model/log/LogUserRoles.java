package auth.webserver.model.log;


import auth.webserver.model.roles.Role;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserRole;
import com.google.gson.Gson;

import java.util.List;


public class LogUserRoles extends LogBase {
    private User userOperator;
    private List<Role> roleList;
    private User user;
    private List<UserRole> userRoleList;

    public LogUserRoles(int logType, User userOperator, User user, List<Role> roleList, List<UserRole> userRoleList) {
        super(logType);
        this.userOperator = userOperator;
        this.roleList = roleList;
        this.user = user;
        this.userRoleList = userRoleList;
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

    private List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    private List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public void fromJson(String src) {
        Gson gson = new Gson();
        LogUserRoles logUserRole = gson.fromJson(src, LogUserRoles.class);
        userOperator = logUserRole.getUserOperator();
        roleList = logUserRole.getRoleList();
        user = logUserRole.getUser();
        userRoleList = logUserRole.getUserRoleList();

    }
}
