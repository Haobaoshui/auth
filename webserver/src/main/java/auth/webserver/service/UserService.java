package auth.webserver.service;


import auth.webserver.model.Page;
import auth.webserver.model.user.LoginFormData;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserSessionInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    //用户默认密码
    String User_Default_Password = "123456";

    String Message_Error_Login_Data_Null = "登录数据为空，请用户填写数据后再次登录";
    String Message_Error_Login_Error_IP = "登录IP地址或域名错误，请输入正确的ip地址或域名";
    String Message_Error_Login_Verification_Code = "验证码错误，请输入正确的验证码";
    String Message_Error_Login_Error_User = "用户名或密码不对，请输入正确的用户名和密码";


    String add(User user);

    int delete(User user);

    int update(User user);

    boolean isExistById(String id);

    boolean isExistByEmplNum(String emplNum);

    boolean isUserCorrect(User user);

    boolean isUserCorrect(String userId, String password);

    boolean isIdentifyCodeCorrect(String code);

    boolean isUserLoginIn(String userId);

    int getCount();

    User getById(String id);

    User getByEmplNum(String emplNum);

    ResponseEntity<UserSessionInfo> doLogin(LoginFormData loginFormData);

    boolean doLogout();

    User changePwd(User user);

    User resetPwd(User user);

    UserSessionInfo getUserSessionInfo(String id);

    UserSessionInfo getUserSessionInfoByEmplNum(String emplNum);

    UserSessionInfo getUserSessionInfo(User user);

    List<User> getAll();

    Page<User> getPage(int pageNo, int pageSize);

    Page<User> getSearchedPage(String searchText, Integer pageNo, Integer pageSize);


}
