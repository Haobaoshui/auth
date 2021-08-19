package auth.webserver.service.impl;


import auth.webserver.model.Page;
import auth.webserver.model.log.*;
import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;
import auth.webserver.model.roles.RolePermissionFormData;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserRole;
import auth.webserver.model.user.UserRoleFormData;
import auth.webserver.model.user.UserSessionInfo;
import auth.webserver.repository.LogsRepository;
import auth.webserver.service.*;
import auth.webserver.utility.SessionUser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LogsServiceImpl implements LogsService {

    private final LogsRepository logsRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;


    @Autowired
    public LogsServiceImpl(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    private static String logType2Value(int value) {
        switch (value) {
            //Logs UserType
            case LogsService.Logs_Type_Value_User_Login:
                return LogsService.Logs_Type_Description_User_Login;
            case LogsService.Logs_Type_Value_User_Logout:
                return LogsService.Logs_Type_Description_User_Logout;
            case LogsService.Logs_Type_Value_User_ChangePwd:
                return LogsService.Logs_Type_Description_User_ChangePwd;
            case LogsService.Logs_Type_Value_User_Add:
                return LogsService.Logs_Type_Description_User_Add;
            case LogsService.Logs_Type_Value_User_Delete:
                return LogsService.Logs_Type_Description_User_Delete;
            case LogsService.Logs_Type_Value_User_Update:
                return LogsService.Logs_Type_Description_User_Update;

            //Role Logs
            case LogsService.Logs_Type_Value_Role_Add:
                return LogsService.Logs_Type_Description_Role_Add;
            case LogsService.Logs_Type_Value_Role_Delete:
                return LogsService.Logs_Type_Description_Role_Delete;
            case LogsService.Logs_Type_Value_Role_Update:
                return LogsService.Logs_Type_Description_Role_Update;

            //Permission Logs
            case LogsService.Logs_Type_Value_Permission_Add:
                return LogsService.Logs_Type_Description_Permission_Add;
            case LogsService.Logs_Type_Value_Permission_Delete:
                return LogsService.Logs_Type_Description_Permission_Delete;
            case LogsService.Logs_Type_Value_Permission_Update:
                return LogsService.Logs_Type_Description_Permission_Update;

            //RolePermission Logs
            case LogsService.Logs_Type_Value_RolePermission_Add:
                return LogsService.Logs_Type_Description_RolePermission_Add;
            case LogsService.Logs_Type_Value_RolePermission_Delete:
                return LogsService.Logs_Type_Description_RolePermission_Delete;
            case LogsService.Logs_Type_Value_RolePermission_Update:
                return LogsService.Logs_Type_Description_RolePermission_Update;

            //UserRole Logs
            case LogsService.Logs_Type_Value_UserRole_Add:
                return LogsService.Logs_Type_Description_UserRole_Add;
            case LogsService.Logs_Type_Value_UserRole_Delete:
                return LogsService.Logs_Type_Description_UserRole_Delete;
            case LogsService.Logs_Type_Value_UserRole_Update:
                return LogsService.Logs_Type_Description_UserRole_Update;
            case LogsService.Logs_Type_Value_UserRoles_Add:
                return LogsService.Logs_Type_Description_UserRoles_Add;
            case LogsService.Logs_Type_Value_UserRoles_Delete:
                return LogsService.Logs_Type_Description_UserRoles_Delete;
            case LogsService.Logs_Type_Value_UserRoles_Update:
                return LogsService.Logs_Type_Description_UserRoles_Update;






        }
        return "未知操作";
    }



    private List<LogsStatistics> convert(List<Integer> typeList) {
        if (typeList == null || typeList.size() == 0) return null;

        List<LogsStatistics> logsStatisticsList = new ArrayList<>();
        for (Integer i : typeList) {
            LogsStatistics logsStatistics = new LogsStatistics();
            logsStatistics.setValue(logsRepository.getCountByLogType(i));
            logsStatistics.setTypeName(LogsServiceImpl.logType2Value(i));
            logsStatisticsList.add(logsStatistics);
        }
        return logsStatisticsList;
    }

    @Override
    public int deleteById(String id) {
        return logsRepository.deleteById(id);
    }

    @Override
    public int deleteByUserId(String userId) {
        return logsRepository.deleteByUserId(userId);
    }

    @Override
    public int delete(Logs logs) {
        return logsRepository.delete(logs);
    }

    @Override
    public int delete(User user) {
        return logsRepository.delete(user);
    }

    @Override
    public int deleteAll() {
        return logsRepository.deleteAll();
    }

    @Override
    public int update(Logs logs) {
        return logsRepository.update(logs);
    }

    @Override
    public int update(String id, String userId, String logIp, int logType, String logContent) {
        return logsRepository.update(id, userId, logIp, logType, logContent);
    }

    @Override
    public boolean isExistById(String id) {
        return logsRepository.isExistById(id);
    }

    @Override
    public int getCount() {
        return logsRepository.getCount();
    }

    @Override
    public Logs getById(String id) {
        return logsRepository.getById(id);
    }

    @Override
    public List<LogsStatistics> getStatisticsList(String searchText, Date dateBegin, Date dateEnd) {
//        List<Integer> typeList = null;
//
//        if (searchText == null && (dateBegin == null || dateEnd == null)) typeList = logsRepository.getAllLogTypes();
//        else
//            typeList = logsRepository.getAllLogTypesUserLike(searchText, dateBegin, dateEnd);
//        return convert(typeList);
        List<Integer> typeList = null;
        if (searchText == null && (dateBegin == null || dateEnd == null)) {
            typeList = logsRepository.getAllLogTypes();
        } else {
            typeList = logsRepository.getAllLogTypesUserLike(searchText, dateBegin, dateEnd);
        }
        return convert(typeList);

    }

    @Override
    public List<LogsStatistics> getStatisticsList(String userId) {
        List<Integer> typeList = logsRepository.getAllLogTypes(userId);


        return convert(typeList);
    }

    String getDescriptionFromRoleJson(String json) {
        try {
            Gson gson = new Gson();
            LogRole logRole = gson.fromJson(json, LogRole.class);
            return logRole.getRole().getName();
        } catch (Exception e) {

        }
        return null;
    }

    String getDescriptionFromPermissionJson(String json) {
        try {
            Gson gson = new Gson();
            LogPermission logPermission = gson.fromJson(json, LogPermission.class);
            return logPermission.getPermission().getName();
        } catch (Exception e) {

        }
        return null;
    }

    String getDescriptionFromRolePermissionJson(String json) {
        try {
            Gson gson = new Gson();
            LogRolePermission logRolePermission = gson.fromJson(json, LogRolePermission.class);
            return logRolePermission.getRole().getName() + "-" + logRolePermission.getPermission().getName();
        } catch (Exception e) {

        }
        return null;
    }

    String getDescriptionFromUserRoleJson(String json) {
        try {
            Gson gson = new Gson();
            LogUserRole logUserRole = gson.fromJson(json, LogUserRole.class);
            return logUserRole.getUser().getEmplName() + "-" + logUserRole.getRole().getName();
        } catch (Exception e) {

        }
        return null;
    }


    private List<LogsView> convertView(List<Logs> logsList) {
        List<LogsView> logsViewList = new ArrayList<>();


        for (Logs logs : logsList) {

            LogsView logsView = new LogsView();

            logsView.setLogs(logs);

            User user = userService.getById(logs.getUserId());
            logsView.setUserOperator(user);

            String json = logs.getLogContent();
            int logType = logs.getLogType();
            String description = LogsServiceImpl.logType2Value(logs.getLogType());
            String specDesc = null;
            switch (logType) {
                //Logs UserType
                case LogsService.Logs_Type_Value_User_Login:

                case LogsService.Logs_Type_Value_User_Logout:

                case LogsService.Logs_Type_Value_User_ChangePwd:

                case LogsService.Logs_Type_Value_User_Add:

                case LogsService.Logs_Type_Value_User_Delete:

                case LogsService.Logs_Type_Value_User_Update:


                    //Role Logs
                case LogsService.Logs_Type_Value_Role_Add:

                case LogsService.Logs_Type_Value_Role_Delete:

                case LogsService.Logs_Type_Value_Role_Update:

                    specDesc = getDescriptionFromRoleJson(json);

                    break;

                //Permission Logs
                case LogsService.Logs_Type_Value_Permission_Add:

                case LogsService.Logs_Type_Value_Permission_Delete:

                case LogsService.Logs_Type_Value_Permission_Update:
                    specDesc = getDescriptionFromPermissionJson(json);

                    break;


                //RolePermission Logs
                case LogsService.Logs_Type_Value_RolePermission_Add:

                case LogsService.Logs_Type_Value_RolePermission_Delete:

                case LogsService.Logs_Type_Value_RolePermission_Update:
                    specDesc = getDescriptionFromRolePermissionJson(json);

                    break;


                //UserRole Logs
                case LogsService.Logs_Type_Value_UserRole_Add:

                case LogsService.Logs_Type_Value_UserRole_Delete:

                case LogsService.Logs_Type_Value_UserRole_Update:

                case LogsService.Logs_Type_Value_UserRoles_Add:

                case LogsService.Logs_Type_Value_UserRoles_Delete:

                case LogsService.Logs_Type_Value_UserRoles_Update:
                    specDesc = getDescriptionFromUserRoleJson(json);

                    break;




            }


            if (specDesc != null && specDesc.length() > 0)
                description = description + ":" + specDesc;


            logsView.setDescription(description);
            logsViewList.add(logsView);


        }
        return logsViewList;
    }

    @Override
    public Page<LogsView> getPage(Date dateBeginDate, Date dateEndDate, int pageNo, int pageSize) {

        Page<Logs> logsPage = logsRepository.getPage(dateBeginDate, dateEndDate, pageNo, pageSize);
        if (logsPage == null) {
            return null;
        }
        return new Page<>(logsPage.getStart(), logsPage.getTotalCount(), pageSize, convertView(logsPage.getResult()));
    }

    @Override
    public Page<LogsView> getPage(String userId, int pageNo, int pageSize) {
        Page<Logs> logsPage = logsRepository.getPage(userId, pageNo, pageSize);
        if (logsPage == null) return null;


        return new Page<>(logsPage.getStart(), logsPage.getTotalCount(), pageSize, convertView(logsPage.getResult()));
    }

    @Override
    public Page<LogsView> getPage(String searchText, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize) {
        Page<Logs> logsPage = logsRepository.getPage(searchText, dateBegin, dateEnd, pageNo, pageSize);
        if (logsPage == null) return null;
        return new Page<>(logsPage.getStart(), logsPage.getTotalCount(), pageSize, convertView(logsPage.getResult()));
    }

    @Override
    public Page<LogsView> getPageByUserId(String userId, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize) {
        Page<Logs> logsPage = logsRepository.getPageByUserId(userId, dateBegin, dateEnd, pageNo, pageSize);
        if (logsPage == null) return null;


        return new Page<>(logsPage.getStart(), logsPage.getTotalCount(), pageSize, convertView(logsPage.getResult()));
    }

    private void add(UserSessionInfo userinfo, LogUser logUser) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logUser.getLogType(), logUser.toString());
    }

    private void add(UserSessionInfo userinfo, LogUsers logUsers) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logUsers.getLogType(), logUsers.toString());
    }

    /**
     * 用户登录
     *
     * @param userinfo
     */
    @Override
    public void loginLog(UserSessionInfo userinfo) {

        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null) return;
        String userId = userOperator.getId();
        LogUser logUser = new LogUser(LogsService.Logs_Type_Value_User_Login, userOperator);
        add(userinfo, logUser);
    }

    /**
     * 用户退出系统
     *
     * @param userinfo
     */
    @Override
    public void logoutLog(UserSessionInfo userinfo) {

        if (userinfo == null) return;
        User userOperator = userinfo.getUser();

        User user = userinfo.getUser();
        if (user == null) return;
        String userId = userinfo.getUser().getId();

        LogUser logUser = new LogUser(LogsService.Logs_Type_Value_User_Logout, userOperator);
        add(userinfo, logUser);
    }

    /**
     * 用户自己修改密码
     *
     * @param user
     */
    @Override
    public void changePwdLog(User user) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        String userId = user.getId();
        LogUser logUser = new LogUser(LogsService.Logs_Type_Value_User_ChangePwd, user);
        add(userinfo, logUser);
    }

    /**
     * 管理员修改用户密码
     *
     * @param user
     */
    @Override
    public void resetPwdLog(User user) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        String userId = user.getId();
        LogUsers logUsers = new LogUsers(LogsService.Logs_Type_Value_User_ChangePwd, userOperator, user);
        add(userinfo, logUsers);
    }

    /**
     * 增加用户
     *
     * @param user
     */
    @Override
    public void addLog(User user) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || user == null) return;

        LogUsers logUsers = new LogUsers(LogsService.Logs_Type_Value_User_Add, userOperator, user);
        add(userinfo, logUsers);
    }

    /**
     * 删除用户
     *
     * @param user
     */
    @Override
    public void deleteLog(User user) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || user == null) return;

        LogUsers logUsers = new LogUsers(LogsService.Logs_Type_Value_User_Delete, userOperator, user);
        add(userinfo, logUsers);
    }

    /**
     * 用户更新
     *
     * @param user
     */
    @Override
    public void updateLog(User user) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || user == null) return;

        LogUsers logUsers = new LogUsers(LogsService.Logs_Type_Value_User_Update, userOperator, user);
        add(userinfo, logUsers);
    }

    private void add(UserSessionInfo userinfo, LogRole logRole) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logRole.getLogType(), logRole.toString());
    }

    @Override
    public void addLog(Role role) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || role == null) return;

        LogRole logRole = new LogRole(LogsService.Logs_Type_Value_Role_Add, userOperator, role);
        add(userinfo, logRole);
    }

    @Override
    public void deleteLog(Role role) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || role == null) return;

        LogRole logRole = new LogRole(LogsService.Logs_Type_Value_Role_Add, userOperator, role);
        add(userinfo, logRole);
    }

    @Override
    public void updateLog(Role role) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || role == null) return;

        LogRole logRole = new LogRole(LogsService.Logs_Type_Value_Role_Update, userOperator, role);
        add(userinfo, logRole);
    }

    private void add(UserSessionInfo userinfo, LogPermission logPermission) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logPermission.getLogType(), logPermission.toString());
    }

    @Override
    public void addLog(Permission permission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || permission == null) return;

        LogPermission logPermission = new LogPermission(LogsService.Logs_Type_Value_Permission_Add, userOperator, permission);
        add(userinfo, logPermission);
    }

    @Override
    public void deleteLog(Permission permission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || permission == null) return;

        LogPermission logPermission = new LogPermission(LogsService.Logs_Type_Value_Permission_Delete, userOperator, permission);
        add(userinfo, logPermission);
    }

    @Override
    public void updateLog(Permission permission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || permission == null) return;

        LogPermission logPermission = new LogPermission(LogsService.Logs_Type_Value_Permission_Update, userOperator, permission);
        add(userinfo, logPermission);
    }

    private void add(UserSessionInfo userinfo, LogRolePermission logRolePermission) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logRolePermission.getLogType(), logRolePermission.toString());
    }

    private LogRolePermission convert(int logType, User userOperator, RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getId() == null || rolePermission.getRoleId() == null || rolePermission.getPermissionId() == null)
            return null;

        Permission permission = permissionService.getById(rolePermission.getPermissionId());
        Role role = roleService.getById(rolePermission.getRoleId());

        return new LogRolePermission(logType, userOperator, role, permission, rolePermission);
    }

    @Override
    public void addLog(RolePermission rolePermission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || rolePermission == null) return;
        add(userinfo, convert(LogsService.Logs_Type_Value_RolePermission_Add, userOperator, rolePermission));
    }

    @Override
    public void deleteLog(RolePermission rolePermission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || rolePermission == null) return;
        add(userinfo, convert(LogsService.Logs_Type_Value_RolePermission_Delete, userOperator, rolePermission));
    }

    @Override
    public void updateLog(RolePermission rolePermission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;
        User userOperator = userinfo.getUser();
        if (userOperator == null || rolePermission == null) return;
        add(userinfo, convert(LogsService.Logs_Type_Value_RolePermission_Update, userOperator, rolePermission));
    }

    /**
     * 类型转换
     *
     * @param logType
     * @param userOperator
     * @param rolePermissionFormData
     * @return
     */
    private LogRolePermissions convert(int logType, User userOperator, RolePermissionFormData rolePermissionFormData) {
        if (rolePermissionFormData == null || rolePermissionFormData.getRole() == null || rolePermissionFormData.getPermissionList() == null)
            return null;

        Role role = roleService.getById(rolePermissionFormData.getRole().getId());
        List<Permission> permissionList = new ArrayList<>();
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (Permission permission : rolePermissionFormData.getPermissionList())
            permissionList.add(permissionService.getById(permission.getId()));

        return new LogRolePermissions(logType, userOperator, role, permissionList, rolePermissionList);


    }

    private void add(UserSessionInfo userinfo, LogRolePermissions logRolePermissions) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logRolePermissions.getLogType(), logRolePermissions.toString());
    }

    @Override
    public void updateLog(RolePermissionFormData rolePermissionFormData) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;

        User userOperator = userinfo.getUser();
        if (userOperator == null || rolePermissionFormData == null) return;

        add(userinfo, convert(LogsService.Logs_Type_Value_Permission_Update, userOperator, rolePermissionFormData));
    }

    private void add(UserSessionInfo userinfo, LogUserRole logUserRole) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logUserRole.getLogType(), logUserRole.toString());
    }

    @Override
    public void addLog(UserRole userRole) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;

        User userOperator = userinfo.getUser();
        if (userOperator == null || userRole == null) return;

        User user = userService.getById(userRole.getUserId());
        Role role = roleService.getById(userRole.getRoleId());
        LogUserRole logUserRole = new LogUserRole(LogsService.Logs_Type_Value_UserRole_Add, userOperator, user, role, userRole);
        add(userinfo, logUserRole);
    }

    @Override
    public void deleteLog(UserRole userRole) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;

        User userOperator = userinfo.getUser();
        if (userOperator == null || userRole == null) return;

        User user = userService.getById(userRole.getUserId());
        Role role = roleService.getById(userRole.getRoleId());
        LogUserRole logUserRole = new LogUserRole(LogsService.Logs_Type_Value_UserRole_Delete, userOperator, user, role, userRole);
        add(userinfo, logUserRole);
    }

    @Override
    public void updateLog(UserRole userRole) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;

        User userOperator = userinfo.getUser();
        if (userOperator == null || userRole == null) return;

        User user = userService.getById(userRole.getUserId());
        Role role = roleService.getById(userRole.getRoleId());
        LogUserRole logUserRole = new LogUserRole(LogsService.Logs_Type_Value_UserRole_Update, userOperator, user, role, userRole);
        add(userinfo, logUserRole);
    }

    private void add(UserSessionInfo userinfo, LogUserRoles logUserRoles) {
        if (userinfo == null || userinfo.getUser() == null) return;

        String userId = userinfo.getUser().getId();
        String logIp = userinfo.getLogIP();
        logsRepository.add(userId, logIp, logUserRoles.getLogType(), logUserRoles.toString());
    }

    private LogUserRoles convert(int logType, User userOperator, UserRoleFormData userRoleFormData) {
        if (userRoleFormData == null || userRoleFormData.getUser() == null || userRoleFormData.getRoleList() == null)
            return null;

        User user = userService.getById(userRoleFormData.getUser().getId());
        List<Role> roleList = new ArrayList<>();
        List<UserRole> userRoleList = new ArrayList<>();
        for (Role role : userRoleFormData.getRoleList()) roleList.add(roleService.getById(role.getId()));


        return new LogUserRoles(logType, userOperator, user, roleList, userRoleList);
    }

    @Override
    public void addLog(UserRoleFormData userRoleFormData) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;

        User userOperator = userinfo.getUser();
        if (userOperator == null || userRoleFormData == null) return;

        add(userinfo, convert(LogsService.Logs_Type_Value_UserRoles_Add, userOperator, userRoleFormData));
    }

    @Override
    public void deleteLog(UserRoleFormData userRoleFormData) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;

        User userOperator = userinfo.getUser();
        if (userOperator == null || userRoleFormData == null) return;
        add(userinfo, convert(LogsService.Logs_Type_Value_UserRoles_Delete, userOperator, userRoleFormData));
    }

    //模板日志

    @Override
    public void updateLog(UserRoleFormData userRoleFormData) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return;

        User userOperator = userinfo.getUser();
        if (userOperator == null || userRoleFormData == null) return;

        add(userinfo, convert(LogsService.Logs_Type_Value_UserRoles_Update, userOperator, userRoleFormData));
    }


}
