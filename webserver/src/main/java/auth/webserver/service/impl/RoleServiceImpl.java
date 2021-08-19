package auth.webserver.service.impl;


import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RoleView;
import auth.webserver.model.user.UserSessionInfo;
import auth.webserver.repository.RoleRepository;
import auth.webserver.service.LogsService;
import auth.webserver.service.RolePermissionService;
import auth.webserver.service.RoleService;
import auth.webserver.utility.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private LogsService logsService;
    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, LogsService logsService) {

        this.roleRepository = roleRepository;
        this.logsService = logsService;
    }

    @Override
    public String add(Role role) {

        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        if (isExistByName(role.getName())) return null;

        String id = roleRepository.add(role);


        if (id != null) {
            role.setId(id);

            //日志:增加用户
            logsService.addLog(role);

        }

        return id;


    }

    @Override
    public String add(String name) {
        return roleRepository.add(name);
    }

    @Override
    public int deleteById(String id) {
        return roleRepository.deleteById(id);
    }

    @Override
    public int delete(Role role) {

        if (role == null || role.getId() == null) return 0;

        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;

        Role roleDeleted = getById(role.getId());
        if (roleDeleted == null) return 0;

        int result = roleRepository.delete(role);

        //日志:删除用户
        logsService.deleteLog(roleDeleted);

        return result;
    }

    @Override
    public int deleteAll() {
        return roleRepository.deleteAll();
    }

    @Override
    public int update(Role role) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;

        if (!roleRepository.isExistById(role.getId())) return 0;

        int result = roleRepository.update(role);

        role = getById(role.getId());


        //日志:更新用户
        logsService.updateLog(role);

        return result;

    }

    @Override
    public int update(String id, String name) {
        return roleRepository.update(id, name);
    }

    @Override
    public boolean isExistById(String id) {
        return roleRepository.isExistById(id);
    }

    @Override
    public boolean isExistByName(String name) {
        return roleRepository.isExistByName(name);
    }

    @Override
    public int getCount() {
        return roleRepository.getCount();
    }

    @Override
    public int getLikeNameCount(String name) {
        return roleRepository.getLikeNameCount(name);
    }

    @Override
    public Role getById(String id) {
        return roleRepository.getById(id);
    }

    @Override
    public List<Role> getByLikeName(String name) {
        return roleRepository.getByLikeName(name);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.getAll();
    }

    @Override
    public Page<Role> getPage(int pageNo, int pageSize) {
        return roleRepository.getPage(pageNo, pageSize);
    }

    @Override
    public Page<Role> getSearchedPage(String searchText, Integer pageNo, Integer pageSize) {
        return roleRepository.getPage(searchText, pageNo, pageSize);
    }

    @Override
    public List<RoleView> getAllRoleViews() {
        List<Role> roleList = roleRepository.getAll();
        if (roleList == null || roleList.size() == 0) return null;

        List<RoleView> roleViewList = new ArrayList<>();
        for (Role role : roleList) {
            RoleView roleView = new RoleView();
            roleView.setRole(role);
            roleView.setPermissionList(rolePermissionService.getPermissionsByRoleId(role.getId()));
            roleViewList.add(roleView);
        }
        return roleViewList;

    }

    @Override
    public Page<RoleView> getViewPage(int pageNo, int pageSize) {
        Page<Role> rolePage = roleRepository.getPage(pageNo, pageSize);
        if (rolePage == null) return null;


        List<RoleView> roleViewList = new ArrayList<>();


        for (Role role : rolePage.getResult()) {

            RoleView roleView = new RoleView();
            roleView.setRole(role);
            roleView.setPermissionList(rolePermissionService.getPermissionsByRoleId(role.getId()));
            roleViewList.add(roleView);


        }
        return new Page<>(rolePage.getStart(), rolePage.getTotalCount(), pageSize, roleViewList);
    }
}
