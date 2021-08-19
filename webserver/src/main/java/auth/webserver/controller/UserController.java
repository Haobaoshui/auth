package auth.webserver.controller;



import auth.webserver.configure.CommonConstant;
import auth.webserver.model.Page;
import auth.webserver.model.user.LoginFormData;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserSessionInfo;
import auth.webserver.service.UserService;
import auth.webserver.utility.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/v1")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 获得验证码
     *
     * @return
     */
    @RequestMapping(value = "identifycode", method = RequestMethod.GET)
    public String getIdentifyCode() {
        return SessionUser.genCode();
    }

    @RequestMapping(value = "isrightidcode", method = RequestMethod.GET)
    public boolean isIdentifyCodeCorrect(@RequestBody String code) {
        return userService.isIdentifyCodeCorrect(code);
    }

    /**
     * 登录
     *
     * @param loginFormData
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<UserSessionInfo> doLogin(@RequestBody LoginFormData loginFormData) {
        return userService.doLogin(loginFormData);
    }

    /**
     * 登录
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public boolean doLogout() {
        return userService.doLogout();
    }


    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "changepwd", method = RequestMethod.PUT)
    public User changepwd(@RequestBody User user) {
        return userService.changePwd(user);
    }

    @RequestMapping(value = "resetpwd", method = RequestMethod.PUT)
    public User resetpwd(@RequestBody User user) {
        return userService.resetPwd(user);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody User user) {
        return userService.add(user);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public int delete(@RequestBody User user) {
        return userService.delete(user);
    }

    @RequestMapping(value = "isexist", method = RequestMethod.GET)
    public boolean isExistByEmployeeNum(@RequestBody String employeeNum) {
        if (employeeNum != null && employeeNum.length() > 0) return userService.isExistByEmplNum(employeeNum);
        return false;
    }

    /**
     * 判断用户是否登录
     *
     * @param userId userId
     * @return
     */
    @RequestMapping(value = "isUserLogin", method = RequestMethod.GET)
    public boolean isUserLoginIn(@RequestParam(value = "userId", required = true) String userId) {
        if (userId != null && userId.length() > 0) return userService.isUserLoginIn(userId);
        return false;
    }

    /**
     * 判断用户id和密码是否正确
     *
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping(value = "isusercorrect", method = RequestMethod.GET)
    public boolean isUserCorrect(@RequestParam(value = "userId", required = true) String userId,
                                 @RequestParam(value = "password", required = true) String password) {
        if (userId != null && userId.length() > 0 && password != null && password.length() > 0)
            return userService.isUserCorrect(userId, password);
        return false;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public int update(@RequestBody User user) {
        return userService.update(user);
    }


    @RequestMapping(value = "page", method = RequestMethod.GET)
    public Page<User> getPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userService.getPage(pageNo, pageSize);
    }

    @RequestMapping(value = "searchpage", method = RequestMethod.GET)
    public Page<User> getSearchedPage(@RequestParam(value = "searchText", required = true) String searchText,
                                      @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userService.getSearchedPage(searchText, pageNo, pageSize);
    }


}
