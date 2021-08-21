package auth.webserver.controller;


import auth.webserver.configure.CommonConstant;
import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.user.*;
import auth.webserver.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("userrole/v1")
public class UserRoleController {

    private final UserRoleService userRoleService;



    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping( "add")
    public String add(@RequestBody UserRole userRole) {
        return userRoleService.add( userRole);
    }

    @PostMapping( "adds")
    public String add(@RequestBody List<UserRole> userRoleList) {
        return userRoleService.add( userRoleList);
    }

    @PostMapping( "addroles")
    public String add(@RequestBody UserRoleFormData userRoleFormData) {
        return userRoleService.add( userRoleFormData);
    }


    @DeleteMapping( "delete")
    public int delete(@RequestBody UserRole userRole) {

        return userRoleService.delete( userRole);
    }

    @DeleteMapping( "deletes")
    public int delete(@RequestBody User user) {

        return userRoleService.delete( user);
    }

    @GetMapping( "isexist")
    public boolean isexist(@RequestBody UserRole userRole) {
        if (userRole != null) return userRoleService.isExist(userRole.getUserId(), userRole.getRoleId());
        return false;

    }

    @GetMapping( "permission")
    public boolean hasAuth(@RequestBody UserPermissionFormData userPermissionFormData) {
        if (userPermissionFormData != null) return userRoleService.isExistPermission(userPermissionFormData);
        return false;

    }

    @PutMapping( "update")
    public int update(@RequestBody UserRole userRole) {
        return userRoleService.update( userRole);
    }

    @PutMapping( "updates")
    public int update(@RequestBody UserRoleFormData userRoleFormData) {
        return userRoleService.update( userRoleFormData);
    }

    @GetMapping( "users")
    public List<User> getUsersByRoleId(@RequestParam(value = "roleId", required = true) String roleId) {
        return userRoleService.getUsersByRoleId(roleId);
    }

    @GetMapping("roles")
    public List<Role> getRolesByUserId(@RequestParam(value = "userId", required = true) String userId) {
        return userRoleService.getRolesByUserId(userId);
    }


    @GetMapping( "page")
    public Page<UserRole> getPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userRoleService.getPage(pageNo, pageSize);
    }

    @GetMapping( "viewpage")
    public Page<UserRoleView> getUserRoleViewPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userRoleService.getUserRoleViewPage(pageNo, pageSize);
    }

    @GetMapping( "searchviewpage")
    public Page<UserRoleView> getUserRoleViewPage(@RequestParam(value = "searchText", required = true) String searchText,
                                                  @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userRoleService.getUserRoleViewPage(searchText, pageNo, pageSize);
    }

    @GetMapping( "userpagebyrole")
    public Page<User> getUsersByRoleId(@RequestParam(value = "roleId", required = true) String roleId,
                                       @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userRoleService.getUsersByRoleId(roleId, pageNo, pageSize);
    }

    @GetMapping("searchuserpagebyrole")
    public Page<User> getUsersByRoleId(@RequestParam(value = "searchText", required = true) String searchText,
                                       @RequestParam(value = "roleId", required = true) String roleId,
                                       @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return userRoleService.getUsersByRoleId(searchText, roleId, pageNo, pageSize);
    }


}
