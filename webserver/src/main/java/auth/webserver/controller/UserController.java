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
    @GetMapping("identifycode")
    public String getIdentifyCode() {
        return SessionUser.genCode();
    }

    @GetMapping("isrightidcode")
    public boolean isIdentifyCodeCorrect(@RequestBody String code) {
        return userService.isIdentifyCodeCorrect(code);
    }

    /**
     * 登录
     *
     * @param loginFormData
     * @return
     */
    @PostMapping("login")
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
    @PutMapping("changepwd")
    public User changepwd(@RequestBody User user) {
        return userService.changePwd(user);
    }

    @PutMapping("resetpwd")
    public User resetpwd(@RequestBody User user) {
        return userService.resetPwd(user);
    }


    @PostMapping("user")
    public String add(@RequestBody User user) {
        return userService.add(user);
    }


    @DeleteMapping("user")
    public int delete(@RequestBody User user) {
        return userService.delete(user);
    }

    @GetMapping("isexist")
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
    @GetMapping("isUserLogin")
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
    @GetMapping("isusercorrect")
    public boolean isUserCorrect(@RequestParam(value = "userId", required = true) String userId,
                                 @RequestParam(value = "password", required = true) String password) {
        if (userId != null && userId.length() > 0 && password != null && password.length() > 0)
            return userService.isUserCorrect(userId, password);
        return false;
    }

    @PutMapping("user")
    public int update(@RequestBody User user) {
        return userService.update(user);
    }


    @GetMapping("page")
    public Page<User> getPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userService.getPage(pageNo, pageSize);
    }

    @GetMapping("searchpage")
    public Page<User> getSearchedPage(@RequestParam(value = "searchText", required = true) String searchText,
                                      @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userService.getSearchedPage(searchText, pageNo, pageSize);
    }


}
