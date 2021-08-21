package auth.webserver.controller;



import auth.webserver.configure.CommonConstant;
import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RoleView;
import auth.webserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("role/v1")
public class RoleController {

    private final RoleService roleService;



    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping( "role")
    public String add(@RequestBody Role role) {
        return roleService.add( role);
    }

    @DeleteMapping( "role")
    public int delete(@RequestBody Role role) {

        return roleService.delete( role);
    }

    @GetMapping( "isexist")
    public boolean isexist(@RequestBody String name) {
        if (name != null && name.length() > 0) return roleService.isExistByName(name);
        return false;
    }

    @PutMapping("role")
    public int update(@RequestBody Role role) {
        return roleService.update( role);
    }


    @GetMapping( "viewlist")
    public List<RoleView> getAllRoleViews() {
        return roleService.getAllRoleViews();
    }

    @GetMapping( "list")
    public List<Role> getAll() {
        return roleService.getAll();
    }

    @GetMapping( "page")
    public Page<Role> getPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return roleService.getPage(pageNo, pageSize);


    }

    @GetMapping( "viewpage")
    public Page<RoleView> getViewPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return roleService.getViewPage(pageNo, pageSize);


    }

    @GetMapping( "searchpage")
    public Page<Role> getSearchedPage(@RequestParam(value = "searchText", required = true) String searchText,
                                      @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return roleService.getSearchedPage(searchText, pageNo, pageSize);


    }


}
