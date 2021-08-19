package auth.webserver.service.impl;


import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.user.*;
import auth.webserver.repository.UserRoleRepository;
import auth.webserver.service.*;
import auth.webserver.utility.DebugDump;
import auth.webserver.utility.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    private final LogsService logsService;

    private final RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository,
                               LogsService logsService,
                               RoleService roleService

    ) {
        this.userRoleRepository = userRoleRepository;
        this.logsService = logsService;
        this.roleService = roleService;


    }


    @Override
    public String add(UserRole userRole) {

        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        if (!roleService.isExistById(userRole.getRoleId())) return null;

        if (!userService.isExistById(userRole.getUserId())) return null;

        if (userRoleRepository.isExist(userRole.getUserId(), userRole.getRoleId())) return null;

        String id = userRoleRepository.add(userRole);


        if (id != null) {
            userRole.setId(id);

            //日志:增加用户
            logsService.addLog(userRole);

        }

        return id;

    }

    @Override
    public String add(List<UserRole> userRoleList) {
        if (userRoleList == null || userRoleList.size() == 0) return null;

        String s = "";
        for (UserRole userRole : userRoleList) s += add(userRole);

        return s;
    }

    /**
     * 为一个用户增加多个角色
     *
     * @param userRoleFormData
     * @return
     */
    @Override
    public String add(UserRoleFormData userRoleFormData) {
        if (userRoleFormData == null || userRoleFormData.getUser() == null || userRoleFormData.getUser().getId() == null
                || userRoleFormData.getRoleList() == null || userRoleFormData.getRoleList().size() == 0) return null;


        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        for (Role role : userRoleFormData.getRoleList()) if (!roleService.isExistById(role.getId())) return null;

        String userId = userRoleFormData.getUser().getId();
        if (!userService.isExistById(userId)) return null;

        List<UserRole> userRoleList = new ArrayList<>();
        for (Role role : userRoleFormData.getRoleList()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(role.getId());
            userRoleList.add(userRole);

        }
        return add(userRoleList);


    }

    @Override
    public String add(String userId, String roleId) {
        return userRoleRepository.add(userId, roleId);
    }

    @Override
    public int deleteById(String id) {
        return userRoleRepository.deleteById(id);
    }


    @Override
    public int delete(String userId, String roleId) {
        return userRoleRepository.delete(userId, roleId);
    }

    @Override
    public int delete(UserRole userRole) {
        if (userRole == null) return 0;


        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;


        UserRole userroleDeleted = null;
        if (userRole.getId() != null) {
            DebugDump.printDebugInfo();
            userroleDeleted = getById(userRole.getId());
        } else userroleDeleted = userRoleRepository.getByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId());
        if (userroleDeleted == null) return 0;


        int result = userRoleRepository.delete(userroleDeleted);

        //日志:删除用户
        logsService.deleteLog(userroleDeleted);

        return result;

    }

    @Override
    public int delete(UserRoleFormData userRoleFormData) {
        if (userRoleFormData == null || userRoleFormData.getUser() == null || userRoleFormData.getRoleList() == null)
            return 0;

        //1.确保数据正确
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;

        User user = userService.getById(userRoleFormData.getUser().getId());
        if (user == null) return 0;
        for (Role role : userRoleFormData.getRoleList()) if (!roleService.isExistById(role.getId())) return 0;

        //2删除user原来的角色
        int r = userRoleRepository.delete(user);
        if (r > 0) logsService.deleteLog(userRoleFormData);

        return r;
    }

    @Override
    public int delete(User user) {
        if (user == null || user == null) return 0;


        //1.确保数据正确
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;


        if (!userService.isExistById(user.getId())) return 0;


        List<UserRole> userRoleList = userRoleRepository.getByUserId(user.getId());

        //2删除user原来的角色
        int r = userRoleRepository.delete(user);
        if (r > 0) {
            UserRoleFormData userRoleFormData = new UserRoleFormData();
            userRoleFormData.setUser(user);

            List<Role> roleList = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                Role role = roleService.getById(userRole.getRoleId());
                if (role != null) roleList.add(role);
            }
            userRoleFormData.setRoleList(roleList);

            logsService.deleteLog(userRoleFormData);
        }

        return r;
    }

    @Override
    public int deleteAll() {
        return userRoleRepository.deleteAll();
    }

    @Override
    public int update(UserRole userRole) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;
        if (!userRoleRepository.isExistById((userRole.getId()))) return 0;

        int result = userRoleRepository.update(userRole);

        userRole = getById(userRole.getId());
        if (userRole == null) return 0;

        //日志:更新用户
        logsService.updateLog(userRole);

        return result;

    }

    @Override
    public int update(UserRoleFormData userRoleFormData) {
        if (userRoleFormData == null || userRoleFormData.getUser() == null || userRoleFormData.getRoleList() == null)
            return 0;

        //1.确保数据正确
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;

        User user = userService.getById(userRoleFormData.getUser().getId());
        if (user == null) return 0;
        for (Role role : userRoleFormData.getRoleList()) if (!roleService.isExistById(role.getId())) return 0;

        //2删除user原来的角色
        userRoleRepository.delete(user);

        int result = 0;

        //3为user增加新的角色
        for (Role role : userRoleFormData.getRoleList()) {
            String r = userRoleRepository.add(user.getId(), role.getId());
            if (r != null) result += 1;
        }
        if (result > 0) logsService.updateLog(userRoleFormData);

        return result;
    }

    @Override
    public int update(String id, String userId, String roleId) {
        return userRoleRepository.update(id, userId, roleId);
    }

    @Override
    public boolean isExistById(String id) {
        return userRoleRepository.isExistById(id);
    }

    @Override
    public boolean isExist(String userId, String roleId) {
        return userRoleRepository.isExist(userId, roleId);
    }

    @Override
    public boolean isExistPermission(String userId, String permissionId) {
        return userRoleRepository.isExistPermission(userId, permissionId);
    }

    @Override
    public int getCount() {
        return userRoleRepository.getCount();
    }

    @Override
    public int getCountByUserId(String userId) {
        return userRoleRepository.getCountByUserId(userId);
    }

    @Override
    public int getCountByRoleId(String roleId) {
        return userRoleRepository.getCountByRoleId(roleId);
    }

    @Override
    public UserRole getById(String id) {
        return userRoleRepository.getById(id);
    }

    @Override
    public List<UserRole> getAll() {
        return userRoleRepository.getAll();
    }

    @Override
    public List<UserRole> getByUserId(String userId) {
        return userRoleRepository.getByUserId(userId);
    }

    @Override
    public List<UserRole> getByRoleId(String roleId) {
        return userRoleRepository.getByRoleId(roleId);
    }

    @Override
    public Page<UserRole> getPage(int pageNo, int pageSize) {
        return userRoleRepository.getPage(pageNo, pageSize);
    }

    private List<UserRoleView> convertUserIds2UserRoleViewList(List<String> userIdList) {
        List<UserRoleView> userRoleViewList = new ArrayList<>();


        for (String id : userIdList) {

            UserRoleView userRoleView = new UserRoleView();
            User user = userService.getById(id);
            userRoleView.setUser(user);
            userRoleView.setRoleList(getRolesByUserId(id));
            userRoleViewList.add(userRoleView);
        }
        return userRoleViewList;
    }

    @Override
    public Page<UserRoleView> getUserRoleViewPage(Integer pageNo, Integer pageSize) {

        Page<String> userPage = userRoleRepository.getUserIdsPage(pageNo, pageSize);
        if (userPage == null) return null;


        return new Page<>(userPage.getStart(), userPage.getTotalCount(), pageSize, convertUserIds2UserRoleViewList(userPage.getResult()));
    }

    @Override
    public Page<UserRoleView> getUserRoleViewPage(String searchText, Integer pageNo, Integer pageSize) {
        Page<String> userPage = userRoleRepository.getUserIdsPage(searchText, pageNo, pageSize);
        if (userPage == null) return null;


        return new Page<>(userPage.getStart(), userPage.getTotalCount(), pageSize, convertUserIds2UserRoleViewList(userPage.getResult()));
    }

    @Override
    public List<Role> getRolesByUserId(String userId) {
        List<UserRole> userRoleList = getByUserId(userId);
        if (userRoleList == null || userRoleList.size() == 0) return null;

        List<Role> roleList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            Role role = roleService.getById(userRole.getRoleId());
            if (role != null) roleList.add(role);
        }
        return roleList;
    }

    @Override
    public List<User> getUsersByRoleId(String roleId) {
        List<UserRole> userRoleList = getByRoleId(roleId);
        if (userRoleList == null || userRoleList.size() == 0) return null;

        List<User> userList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            User user = userService.getById(userRole.getUserId());
            if (user != null) userList.add(user);
        }
        return userList;
    }

    private List<User> convertUserIds2UserList(List<String> userIdList) {
        List<User> userList = new ArrayList<>();


        for (String id : userIdList) {
            User user = userService.getById(id);
            userList.add(user);
        }

        return userList;
    }

    @Override
    public Page<User> getUsersByRoleId(String roleId, Integer pageNo, Integer pageSize) {
        Page<String> userPage = userRoleRepository.getUserIdsPageByRoleId(roleId, pageNo, pageSize);
        if (userPage == null) return null;

        return new Page<>(userPage.getStart(), userPage.getTotalCount(), pageSize, convertUserIds2UserList(userPage.getResult()));
    }

    @Override
    public Page<User> getUsersByRoleId(String searchText, String roleId, Integer pageNo, Integer pageSize) {
        Page<String> userPage = userRoleRepository.getUserIdsPageByRoleId(searchText, roleId, pageNo, pageSize);
        if (userPage == null) return null;


        return new Page<>(userPage.getStart(), userPage.getTotalCount(), pageSize, convertUserIds2UserList(userPage.getResult()));
    }

    @Override
    public boolean isExistPermission(UserPermissionFormData userPermissionFormData) {
        if (userPermissionFormData == null || userPermissionFormData.getUser() == null) return false;
        User user = userPermissionFormData.getUser();
        String permissionId = null;
        switch (userPermissionFormData.getType()) {
            case 0:
                permissionId = PermissionService.Permission_ID_authUserRole_ViewAll;
                break;
            case 1:
                permissionId = PermissionService.Permission_ID_authUserRole_Add;
                break;
            case 2:
                permissionId = PermissionService.Permission_ID_authUserRole_Delete;
                break;
            case 3:
                permissionId = PermissionService.Permission_ID_authUserRole_Update;
                break;
            default:
                return false;
        }
        return userRoleRepository.isExistPermission(user.getId(), permissionId);
    }
}
