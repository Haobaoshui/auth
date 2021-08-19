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

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody Role role) {
        return roleService.add( role);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public int delete(@RequestBody Role role) {

        return roleService.delete( role);
    }

    @RequestMapping(value = "isexist", method = RequestMethod.GET)
    public boolean isexist(@RequestBody String name) {
        if (name != null && name.length() > 0) return roleService.isExistByName(name);
        return false;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public int update(@RequestBody Role role) {
        return roleService.update( role);
    }


    @RequestMapping(value = "viewlist", method = RequestMethod.GET)
    public List<RoleView> getAllRoleViews() {
        return roleService.getAllRoleViews();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Role> getAll() {
        return roleService.getAll();
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    public Page<Role> getPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return roleService.getPage(pageNo, pageSize);


    }

    @RequestMapping(value = "viewpage", method = RequestMethod.GET)
    public Page<RoleView> getViewPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return roleService.getViewPage(pageNo, pageSize);


    }

    @RequestMapping(value = "searchpage", method = RequestMethod.GET)
    public Page<Role> getSearchedPage(@RequestParam(value = "searchText", required = true) String searchText,
                                      @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return roleService.getSearchedPage(searchText, pageNo, pageSize);


    }


}
