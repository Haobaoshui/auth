package auth.webserver.controller;


import auth.webserver.configure.CommonConstant;
import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;
import auth.webserver.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("permission/v1")
public class PermissionController {

    private final PermissionService permissionService;



    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping( "permission")
    public String add(@RequestBody Permission permission) {
        return permissionService.add(permission);
    }

    @DeleteMapping( "permission")
    public int delete(@RequestBody Permission permission) {

        return permissionService.delete(permission);
    }

    @GetMapping( "isexist")
    public boolean isexist(@RequestBody String name) {
        if (name != null && name.length() > 0) return permissionService.isExistByName(name);
        return false;
    }

    @PutMapping( "permission")
    public int update(@RequestBody Permission permission) {
        return permissionService.update(permission);
    }


    @GetMapping( "list")
    public List<Permission> getAllRoleViews() {
        return permissionService.getAll();
    }

    @GetMapping( "page")
    public Page<Permission> getPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return permissionService.getPage(pageNo, pageSize);


    }


}
