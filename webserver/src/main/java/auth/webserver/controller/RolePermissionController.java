package auth.webserver.controller;


import auth.webserver.configure.CommonConstant;
import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;
import auth.webserver.model.roles.RolePermissionFormData;
import auth.webserver.service.RolePermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("rolepermission/v1")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;



    @Autowired
    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody RolePermission rolePermission) {
        return rolePermissionService.add( rolePermission);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public int delete(@RequestBody RolePermission rolePermission) {

        return rolePermissionService.delete( rolePermission);
    }

    @RequestMapping(value = "isexist", method = RequestMethod.GET)
    public boolean isexist(@RequestBody RolePermission rolePermission) {
        if (rolePermission != null)
            return rolePermissionService.isExist(rolePermission.getRoleId(), rolePermission.getPermissionId());
        return false;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public int update(@RequestBody RolePermission rolePermission) {
        return rolePermissionService.update( rolePermission);
    }

    @RequestMapping(value = "updates", method = RequestMethod.PUT)
    public int update(@RequestBody RolePermissionFormData rolePermissionFormData) {

        return rolePermissionService.update( rolePermissionFormData);
    }


    @RequestMapping(value = "permissions", method = RequestMethod.GET)
    public List<Permission> getPermissionsByRoleId(@RequestParam(value = "roleId", required = true) String roleId) {
        return rolePermissionService.getPermissionsByRoleId(roleId);
    }

    @RequestMapping(value = "roles", method = RequestMethod.GET)
    public List<Role> getRolesByPermissionId(@RequestParam(value = "permissionId", required = true) String permissionId) {
        return rolePermissionService.getRolesByPermissionId(permissionId);
    }


    @RequestMapping(value = "page", method = RequestMethod.GET)
    public Page<RolePermission> getPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return rolePermissionService.getPage(pageNo, pageSize);


    }


}
