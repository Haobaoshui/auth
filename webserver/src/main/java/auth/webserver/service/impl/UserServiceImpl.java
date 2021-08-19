package auth.webserver.service.impl;


import auth.webserver.configure.CommonConstant;
import auth.webserver.model.Page;
import auth.webserver.model.user.LoginFormData;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserSessionInfo;
import auth.webserver.repository.UserRepository;
import auth.webserver.service.LogsService;
import auth.webserver.service.PermissionService;
import auth.webserver.service.UserRoleService;
import auth.webserver.service.UserService;
import auth.webserver.utility.EncoderHandler;
import auth.webserver.utility.ResponseEntityUtils;
import auth.webserver.utility.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LogsService logsService;
    private final UserRoleService userRoleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, LogsService logsService, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.logsService = logsService;
        this.userRoleService = userRoleService;
    }

    @Override
    public String add(User user) {

        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        if (isExistByEmplNum(user.getEmplNum())) return null;

        String id = userRepository.add(user);


        if (id != null) {
            user = getById(user.getId());

            //日志:增加用户
            logsService.addLog(user);

        }

        return id;
    }


    @Override
    public int delete(User user) {
        if (user == null || user.getId() == null) return 0;

        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;

        User userDeleted = getById(user.getId());
        if (userDeleted == null) return 0;


        //删除userrole表数据
        userRoleService.delete(user);
        logsService.delete(user);

        int result = userRepository.deleteById(user.getId());


        //日志:删除用户
        logsService.deleteLog(userDeleted);


        return result;

    }


    @Override
    public int update(User user) {


        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;

        if (!userRepository.isExistById(user.getId())) return 0;

        int result = userRepository.update(user);

        user = getById(user.getId());


        //日志:更新用户
        logsService.updateLog(user);

        return result;

    }


    @Override
    public boolean isExistById(String id) {
        return userRepository.isExistById(id);
    }

    @Override
    public boolean isExistByEmplNum(String emplNum) {
        return userRepository.isExistByEmplNum(emplNum);
    }

    /**
     * 验证用户是否正确（验证工号和密码），注意user中密码为明文
     *
     * @param user
     * @return
     */
    @Override
    public boolean isUserCorrect(User user) {
        if (user == null || user.getEmplNum() == null || user.getEmplPwd() == null) return false;

        User userDB = getByEmplNum(user.getEmplNum());
        if (userDB == null) return false;
        String encodePwd = EncoderHandler.encode(user.getEmplPwd());
        if (encodePwd.equals(userDB.getEmplPwd())) return true;
        return false;

    }

    @Override
    public boolean isUserCorrect(String userId, String password) {

        if (userId == null || password == null) return false;

        User user = getById(userId);
        if (user == null) return false;
        String encodePwd = EncoderHandler.encode(password);
        if (encodePwd.equals(user.getEmplPwd())) return true;
        return false;
    }

    @Override
    public boolean isIdentifyCodeCorrect(String code) {
        //检查验证码是否正确
        String correctCode = SessionUser.getCode();

        if (correctCode.equals(code)) return true;
        return false;
    }

    @Override
    public boolean isUserLoginIn(String userId) {
        if(userId==null || userId.length()==0)
            return false;
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if(userinfo==null)
            return false;
        User user=userinfo.getUser();
        if(user==null)
            return false;

        if(userId.equals(user.getId()))
            return true;

        return false;
    }

    @Override
    public int getCount() {
        return userRepository.getCount();
    }

    @Override
    public User getById(String id) {

        User user = userRepository.getById(id);

        return user;


    }

    @Override
    public User getByEmplNum(String emplNum) {
        return userRepository.getByEmplNum(emplNum);
    }

    @Override
    public ResponseEntity<UserSessionInfo> doLogin(LoginFormData loginFormData) {
        if (loginFormData == null || loginFormData.getUser() == null
                || loginFormData.getIdentifyCode() == null || loginFormData.getIdentifyCode().length() == 0)
            return ResponseEntityUtils.badRequest(null, UserService.Message_Error_Login_Data_Null);//登录数据为空，请用户填写数据后再次登录


        //检查验证码是否正确
        String correctCode = SessionUser.getCode();


        if (correctCode == null || !correctCode.equals(loginFormData.getIdentifyCode()))
            return ResponseEntityUtils.badRequest(null, UserService.Message_Error_Login_Verification_Code);//验证码错误，请输入正确的验证码


        User user = loginFormData.getUser();
        if (!isUserCorrect(user))
            return ResponseEntityUtils.badRequest(null, UserService.Message_Error_Login_Error_User);//用户名或密码不对，请输入正确的用户名和密码


        //查询用户是否登录，如果登录则返回session中的对象
        UserSessionInfo userinfo = SessionUser.getSessionUser();

        if (userinfo == null) {

            userinfo = getUserSessionInfoByEmplNum(user.getEmplNum());

            //保存到会话中
            SessionUser.setSessionUser(userinfo);
        }

        //日志:用户登录
        logsService.loginLog(userinfo);

        return ResponseEntity.ok(userinfo);
    }

    @Override
    public boolean doLogout() {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        //记录日志
        logsService.logoutLog(userinfo);


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null){
            return false;
        }

        request.getSession().removeAttribute(CommonConstant.USER_CONTEXT);
        //由SessionListener监听器完成用户退出系统日志和清空Session工作。
        return true;
    }

    @Override
    public User changePwd(User user) {
        //查询用户是否登录，如果登录则返回session中的对象
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        if (userRepository.update(user.getId(), user.getEmplPwd()) > 0) {
            user = getById(user.getId());

            //日志:修改密码
            logsService.changePwdLog(user);
            return user;
        }
        return null;
    }

    /**
     * 重置密码，密码为User_Default_Password
     *
     * @param user
     * @return
     */
    @Override
    public User resetPwd(User user) {
        //查询用户是否登录，如果登录则返回session中的对象
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        if (userRepository.update(user.getId(), UserService.User_Default_Password) > 0) {
            user = getById(user.getId());

            //日志:修改密码
            logsService.resetPwdLog(user);
            return user;
        }
        return null;
    }

    //设置权限
    private void setAuth(UserSessionInfo userSessionInfo, String userId) {
        if (userSessionInfo == null) return;

        /* 用户权限 */
        userSessionInfo.setAuthUserViewAll(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserViewAll));//查看所有用户
        userSessionInfo.setAuthUserAdd(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserAdd));//增加用户
        userSessionInfo.setAuthUserDelete(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserDelete));//删除用户
        userSessionInfo.setAuthUserUpdate(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserUpdate));//修改用户
        userSessionInfo.setAuthUserResetPwd(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserResetPwd));//重置用户密码


        /* 角色权限 */
        userSessionInfo.setAuthRoleViewAll(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRoleViewAll));//查看所有角色
        userSessionInfo.setAuthRoleAdd(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRoleAdd));//增加角色
        userSessionInfo.setAuthRoleDelete(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRoleDelete));//删除角色
        userSessionInfo.setAuthRoleUpdate(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRoleUpdate));//修改角色


        /* 角色权限权限 */
        userSessionInfo.setAuthRolePermissionViewAll(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRolePermissionViewAll));//查看所有角色权限
        userSessionInfo.setAuthRolePermissionAdd(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRolePermissionAdd));//对角色增加权限
        userSessionInfo.setAuthRolePermissionDelete(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRolePermissionDelete));//对角色删除权限
        userSessionInfo.setAuthRolePermissionUpdate(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authRolePermissionUpdate));//对角色修改权限

        /* 用户角色权限 */
        userSessionInfo.setAuthUserRoleViewAll(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserRole_ViewAll));//查看用户的角色
        userSessionInfo.setAuthUserRoleAdd(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserRole_Add));//增加用户的角色
        userSessionInfo.setAuthUserRoleDelete(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserRole_Delete));//删除用户的角色
        userSessionInfo.setAuthUserRoleUpdate(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authUserRole_Update));//修改用户的角色




        /* 日志审计 */

        userSessionInfo.setAuthLogsViewDetail(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authLogs_ViewDetail));//日志查看
        userSessionInfo.setAuthLogsStatistics(userRoleService.isExistPermission(userId, PermissionService.Permission_ID_authLogs_Statistics));//日志统计
    }

    /**
     * 获得用户Session对象，该对象对应一个用户在线时的用户状态
     *
     * @param id
     * @return
     */
    @Override
    public UserSessionInfo getUserSessionInfo(String id) {
        User user = getById(id);
        if (user == null) return null;

        UserSessionInfo userSessionInfo = new UserSessionInfo();
        userSessionInfo.setUser(user);
        setAuth(userSessionInfo, id);

        return userSessionInfo;
    }

    @Override
    public UserSessionInfo getUserSessionInfoByEmplNum(String emplNum) {
        User user = getByEmplNum(emplNum);
        if (user != null) return getUserSessionInfo(user.getId());
        return null;
    }

    @Override
    public UserSessionInfo getUserSessionInfo(User user) {
        if (user != null) return getUserSessionInfo(user.getId());
        return null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public Page<User> getPage(int pageNo, int pageSize) {
        return userRepository.getPage(pageNo, pageSize);
    }

    @Override
    public Page<User> getSearchedPage(String searchText, Integer pageNo, Integer pageSize) {
        return userRepository.getSearchedPage(searchText, pageNo, pageSize);
    }


}
