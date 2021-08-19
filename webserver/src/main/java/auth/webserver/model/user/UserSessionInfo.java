package auth.webserver.model.user;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSessionInfo {
    private User user;
    private List<UserRole> userRoleList;
    private String logIP;//登录ip地址

    /* 用户权限 */
    private boolean authUserViewAll;//查看所有用户
    private boolean authUserAdd;//增加用户
    private boolean authUserDelete;//删除用户
    private boolean authUserUpdate;//修改用户
    private boolean authUserResetPwd;//重置用户密码

    /* 角色权限 */
    private boolean authRoleViewAll;//查看所有角色
    private boolean authRoleAdd;//增加角色
    private boolean authRoleDelete;//删除角色
    private boolean authRoleUpdate;//修改角色

    /* 角色权限权限 */
    private boolean authRolePermissionViewAll;//查看所有角色权限
    private boolean authRolePermissionAdd;//对角色增加权限
    private boolean authRolePermissionDelete;//对角色删除权限
    private boolean authRolePermissionUpdate;//对角色修改权限



    /* 用户角色权限 */
    private boolean authUserRoleViewAll;//查看所有角色权限
    private boolean authUserRoleAdd;//对角色增加权限
    private boolean authUserRoleDelete;//对角色删除权限
    private boolean authUserRoleUpdate;//对角色修改权限



    /* 日志审计 */
    private boolean authLogsViewDetail;//日志查看
    private boolean authLogsStatistics;//日志统计

    public boolean isAuthUserAdd() {
        return authUserAdd;
    }

    public void setAuthUserAdd(boolean authUserAdd) {
        this.authUserAdd = authUserAdd;
    }

    public boolean isAuthUserViewAll() {
        return authUserViewAll;
    }

    public void setAuthUserViewAll(boolean authUserViewAll) {
        this.authUserViewAll = authUserViewAll;
    }

    public boolean isAuthUserDelete() {
        return authUserDelete;
    }

    public void setAuthUserDelete(boolean authUserDelete) {
        this.authUserDelete = authUserDelete;
    }

    public boolean isAuthUserUpdate() {
        return authUserUpdate;
    }

    public void setAuthUserUpdate(boolean authUserUpdate) {
        this.authUserUpdate = authUserUpdate;
    }

    public boolean isAuthUserResetPwd() {
        return authUserResetPwd;
    }

    public void setAuthUserResetPwd(boolean authUserResetPwd) {
        this.authUserResetPwd = authUserResetPwd;
    }

    public boolean isAuthRoleViewAll() {
        return authRoleViewAll;
    }

    public void setAuthRoleViewAll(boolean authRoleViewAll) {
        this.authRoleViewAll = authRoleViewAll;
    }

    public boolean isAuthRoleAdd() {
        return authRoleAdd;
    }

    public void setAuthRoleAdd(boolean authRoleAdd) {
        this.authRoleAdd = authRoleAdd;
    }

    public boolean isAuthRoleDelete() {
        return authRoleDelete;
    }

    public void setAuthRoleDelete(boolean authRoleDelete) {
        this.authRoleDelete = authRoleDelete;
    }

    public boolean isAuthRoleUpdate() {
        return authRoleUpdate;
    }

    public void setAuthRoleUpdate(boolean authRoleUpdate) {
        this.authRoleUpdate = authRoleUpdate;
    }

    public boolean isAuthRolePermissionViewAll() {
        return authRolePermissionViewAll;
    }

    public void setAuthRolePermissionViewAll(boolean authRolePermissionViewAll) {
        this.authRolePermissionViewAll = authRolePermissionViewAll;
    }

    public boolean isAuthRolePermissionAdd() {
        return authRolePermissionAdd;
    }

    public void setAuthRolePermissionAdd(boolean authRolePermissionAdd) {
        this.authRolePermissionAdd = authRolePermissionAdd;
    }

    public boolean isAuthRolePermissionDelete() {
        return authRolePermissionDelete;
    }

    public void setAuthRolePermissionDelete(boolean authRolePermissionDelete) {
        this.authRolePermissionDelete = authRolePermissionDelete;
    }

    public boolean isAuthRolePermissionUpdate() {
        return authRolePermissionUpdate;
    }

    public void setAuthRolePermissionUpdate(boolean authRolePermissionUpdate) {
        this.authRolePermissionUpdate = authRolePermissionUpdate;
    }

    public boolean isAuthUserRoleViewAll() {
        return authUserRoleViewAll;
    }

    public void setAuthUserRoleViewAll(boolean authUserRoleViewAll) {
        this.authUserRoleViewAll = authUserRoleViewAll;
    }

    public boolean isAuthUserRoleAdd() {
        return authUserRoleAdd;
    }

    public void setAuthUserRoleAdd(boolean authUserRoleAdd) {
        this.authUserRoleAdd = authUserRoleAdd;
    }

    public boolean isAuthUserRoleDelete() {
        return authUserRoleDelete;
    }

    public void setAuthUserRoleDelete(boolean authUserRoleDelete) {
        this.authUserRoleDelete = authUserRoleDelete;
    }

    public boolean isAuthUserRoleUpdate() {
        return authUserRoleUpdate;
    }

    public void setAuthUserRoleUpdate(boolean authUserRoleUpdate) {
        this.authUserRoleUpdate = authUserRoleUpdate;
    }

    public boolean isAuthLogsViewDetail() {
        return authLogsViewDetail;
    }

    public void setAuthLogsViewDetail(boolean authLogsViewDetail) {
        this.authLogsViewDetail = authLogsViewDetail;
    }

    public boolean isAuthLogsStatistics() {
        return authLogsStatistics;
    }

    public void setAuthLogsStatistics(boolean authLogsStatistics) {
        this.authLogsStatistics = authLogsStatistics;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public String getLogIP() {
        return logIP;
    }

    public void setLogIP(String logIP) {
        this.logIP = logIP;
    }
}
