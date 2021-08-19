package auth.webserver.service;



import auth.webserver.model.Page;
import auth.webserver.model.log.Logs;
import auth.webserver.model.log.LogsStatistics;
import auth.webserver.model.log.LogsView;
import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;
import auth.webserver.model.roles.RolePermissionFormData;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserRole;
import auth.webserver.model.user.UserRoleFormData;
import auth.webserver.model.user.UserSessionInfo;

import java.util.Date;
import java.util.List;

public interface LogsService {

    //Logs UserType
    int Logs_Type_Value_User_Login = 0;
    int Logs_Type_Value_User_Logout = 1;
    int Logs_Type_Value_User_ChangePwd = 2;
    int Logs_Type_Value_User_Add = 3;
    int Logs_Type_Value_User_Delete = 4;
    int Logs_Type_Value_User_Update = 5;

    //Logs User Description
    String Logs_Type_Description_User_Login = "登录系统";
    String Logs_Type_Description_User_Logout = "退出系统";
    String Logs_Type_Description_User_ChangePwd = "修改密码";
    String Logs_Type_Description_User_Add = "增加用户";
    String Logs_Type_Description_User_Delete = "删除用户";
    String Logs_Type_Description_User_Update = "修改用户";

    //Role Logs
    int Logs_Type_Value_Role_Add = 100;
    int Logs_Type_Value_Role_Delete = 101;
    int Logs_Type_Value_Role_Update = 102;

    String Logs_Type_Description_Role_Add = "增加角色";
    String Logs_Type_Description_Role_Delete = "删除角色";
    String Logs_Type_Description_Role_Update = "修改角色";

    //Permission Logs
    int Logs_Type_Value_Permission_Add = 200;
    int Logs_Type_Value_Permission_Delete = 201;
    int Logs_Type_Value_Permission_Update = 202;

    String Logs_Type_Description_Permission_Add = "增加权限";
    String Logs_Type_Description_Permission_Delete = "删除权限";
    String Logs_Type_Description_Permission_Update = "修改权限";

    //RolePermission Logs
    int Logs_Type_Value_RolePermission_Add = 300;
    int Logs_Type_Value_RolePermission_Delete = 301;
    int Logs_Type_Value_RolePermission_Update = 302;

    String Logs_Type_Description_RolePermission_Add = "增加角色权限";
    String Logs_Type_Description_RolePermission_Delete = "删除角色权限";
    String Logs_Type_Description_RolePermission_Update = "修改角色权限";

    //UserRole Logs
    int Logs_Type_Value_UserRole_Add = 400;
    int Logs_Type_Value_UserRole_Delete = 401;
    int Logs_Type_Value_UserRole_Update = 402;
    int Logs_Type_Value_UserRoles_Add = 450;
    int Logs_Type_Value_UserRoles_Delete = 451;
    int Logs_Type_Value_UserRoles_Update = 452;

    String Logs_Type_Description_UserRole_Add = "增加用户的角色";
    String Logs_Type_Description_UserRole_Delete = "删除用户的角色";
    String Logs_Type_Description_UserRole_Update = "修改用户的角色";

    String Logs_Type_Description_UserRoles_Add = "增加用户的角色";
    String Logs_Type_Description_UserRoles_Delete = "删除用户的角色";
    String Logs_Type_Description_UserRoles_Update = "修改用户的角色";



    int deleteById(String id);

    int deleteByUserId(String userId);

    int delete(Logs logs);

    int delete(User user);

    int deleteAll();

    int update(Logs logs);

    int update(String id, String userId, String logIp, int logType, String logContent);


    boolean isExistById(String id);


    int getCount();

    Logs getById(String id);

    /**
     * 统计数据
     *
     * @return
     */
    List<LogsStatistics> getStatisticsList(String searchText, Date dateBegin, Date dateEnd);

    /**
     * 指定用户的统计数据
     *
     * @param userId
     * @return
     */
    List<LogsStatistics> getStatisticsList(String userId);


    Page<LogsView> getPage(Date dateBeginDate, Date dateEndDate, int pageNo, int pageSize);

    Page<LogsView> getPage(String userId, int pageNo, int pageSize);

    Page<LogsView> getPage(String searchText, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize);

    Page<LogsView> getPageByUserId(String userId, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize);

    //user logs
    void loginLog(UserSessionInfo userinfo);

    void logoutLog(UserSessionInfo userinfo);

    void changePwdLog(User user);

    void resetPwdLog(User user);

    void addLog(User user);

    void deleteLog(User user);

    void updateLog(User user);

    //role logs
    void addLog(Role role);

    void deleteLog(Role role);

    void updateLog(Role role);

    //Permission logs
    void addLog(Permission permission);

    void deleteLog(Permission permission);

    void updateLog(Permission permission);

    //rolepermission logs
    void addLog(RolePermission rolePermission);

    void deleteLog(RolePermission rolePermission);

    void updateLog(RolePermission rolePermission);

    void updateLog(RolePermissionFormData rolePermissionFormData);


    //userrole logs
    void addLog(UserRole userRole);

    void deleteLog(UserRole userRole);

    void updateLog(UserRole userRole);

    void addLog(UserRoleFormData userRoleFormData);

    void deleteLog(UserRoleFormData userRoleFormData);

    void updateLog(UserRoleFormData userRoleFormData);



}
