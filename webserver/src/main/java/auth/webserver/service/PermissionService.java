package auth.webserver.service;



import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;

import java.util.List;

public interface PermissionService {

    /* 用户权限 */
    int Permission_Value_authUserViewAll = 0;//查看所有用户
    int Permission_Value_authUserAdd = 1;//增加用户
    int Permission_Value_authUserDelete = 2;//删除用户
    int Permission_Value_authUserUpdate = 3;//修改用户
    int Permission_Value_authUserResetPwd = 4;//重置用户密码

    String Permission_ID_authUserViewAll = "80000000000000000000000000000001";//查看所有用户
    String Permission_ID_authUserAdd = "80000000000000000000000000000002";//增加用户
    String Permission_ID_authUserDelete = "80000000000000000000000000000004";//删除用户
    String Permission_ID_authUserUpdate = "80000000000000000000000000000008";//修改用户
    String Permission_ID_authUserResetPwd = "80000000000000000000000000000010";//重置用户密码


    /* 角色权限 */
    int Permission_Value_authRoleViewAll = 100;//查看所有角色
    int Permission_Value_authRoleAdd = 101;//增加角色
    int Permission_Value_authRoleDelete = 102;//删除角色
    int Permission_Value_authRoleUpdate = 103;//修改角色

    String Permission_ID_authRoleViewAll = "81000000000000000000000000000001";//查看所有角色
    String Permission_ID_authRoleAdd = "81000000000000000000000000000002";//增加角色
    String Permission_ID_authRoleDelete = "81000000000000000000000000000004";//删除角色
    String Permission_ID_authRoleUpdate = "81000000000000000000000000000008";//修改角色

    /* 角色权限权限 */
    int Permission_Value_authRolePermissionViewAll = 201;//查看所有角色权限
    int Permission_Value_authRolePermissionAdd = 202;//对角色增加权限
    int Permission_Value_authRolePermissionDelete = 203;//对角色删除权限
    int Permission_Value_authRolePermissionUpdate = 204;//对角色修改权限

    String Permission_ID_authRolePermissionViewAll = "82000000000000000000000000000001";//查看所有角色权限
    String Permission_ID_authRolePermissionAdd = "82000000000000000000000000000002";//对角色增加权限
    String Permission_ID_authRolePermissionDelete = "82000000000000000000000000000004";//对角色删除权限
    String Permission_ID_authRolePermissionUpdate = "82000000000000000000000000000008";//对角色修改权限

    //UserRole Logs
    int Permission_Value_authUserRole_ViewAll = 300;//查看用户的角色
    int Permission_Value_authUserRole_Add = 301;//增加用户的角色
    int Permission_Value_authUserRole_Delete = 402;//删除用户的角色
    int Permission_Value_authUserRole_Update = 303;//修改用户的角色

    String Permission_ID_authUserRole_ViewAll = "83000000000000000000000000000001";//查看用户的角色
    String Permission_ID_authUserRole_Add = "83000000000000000000000000000002";//增加用户的角色
    String Permission_ID_authUserRole_Delete = "83000000000000000000000000000004";//删除用户的角色
    String Permission_ID_authUserRole_Update = "83000000000000000000000000000008";//修改用户的角色




    /* 日志审计 */
    int Permission_Value_authLogs_ViewDetail = 602;//删除用户的角色
    int Permission_Value_authLogs_Statistics = 603;//修改用户的角色

    String Permission_ID_authLogs_ViewDetail = "86000000000000000000000000000001";//日志查看
    String Permission_ID_authLogs_Statistics = "86000000000000000000000000000001";//日志统计

    String add(Permission permission);

    String add(String name);

    int deleteById(String id);

    int delete(Permission permission);

    int deleteAll();

    int update(Permission permission);

    int update(String id, String name);


    boolean isExistById(String id);

    boolean isExistByName(String name);

    int getCount();

    int getLikeNameCount(String name);

    Permission getById(String id);

    List<Permission> getByLikeName(String name);

    List<Permission> getAll();

    Page<Permission> getPage(int pageNo, int pageSize);
}
