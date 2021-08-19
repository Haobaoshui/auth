package auth.webserver.service.impl;


import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RolePermission;
import auth.webserver.model.roles.RolePermissionFormData;
import auth.webserver.model.user.UserSessionInfo;
import auth.webserver.repository.RolePermissionRepository;
import auth.webserver.service.LogsService;
import auth.webserver.service.PermissionService;
import auth.webserver.service.RolePermissionService;
import auth.webserver.service.RoleService;
import auth.webserver.utility.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    @Autowired
    LogsService logsService;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;

    @Autowired
    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public String add(RolePermission rolePermission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        if (!roleService.isExistById(rolePermission.getRoleId())) return null;
        if (!permissionService.isExistById(rolePermission.getPermissionId())) return null;

        String id = rolePermissionRepository.add(rolePermission);


        if (id != null) {
            rolePermission.setId(id);

            //日志:增加用户
            logsService.addLog(rolePermission);
        }
        return id;

    }

    @Override
    public String add(String roleId, String permissionId) {
        return rolePermissionRepository.add(roleId, permissionId);
    }

    @Override
    public int deleteById(String id) {
        return rolePermissionRepository.deleteById(id);
    }

    @Override
    public int delete(String roleId, String permissionId) {
        return rolePermissionRepository.delete(roleId, permissionId);
    }

    @Override
    public int delete(RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getId() == null) return 0;


        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;


        RolePermission rolePermissionDeleted = getById(rolePermission.getId());
        if (rolePermissionDeleted == null) return 0;

        int result = rolePermissionRepository.delete(rolePermission);

        //日志:删除用户
        logsService.deleteLog(rolePermissionDeleted);

        return result;

    }

    @Override
    public int deleteAll() {
        return rolePermissionRepository.deleteAll();
    }

    @Override
    public int update(RolePermission rolePermission) {
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;
        if (!rolePermissionRepository.isExistById(rolePermission.getId())) return 0;

        int result = rolePermissionRepository.update(rolePermission);

        rolePermission = getById(rolePermission.getId());


        //日志:更新用户
        logsService.updateLog(rolePermission);

        return result;

    }

    @Override
    public int update(RolePermissionFormData rolePermissionFormData) {
        if (rolePermissionFormData == null || rolePermissionFormData.getRole() == null || rolePermissionFormData.getPermissionList() == null)
            return 0;


        //1.确保数据正确
        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return 0;


        Role role = roleService.getById(rolePermissionFormData.getRole().getId());
        if (role == null) return 0;


        for (Permission permission : rolePermissionFormData.getPermissionList())
            if (!permissionService.isExistById(permission.getId())) return 0;

        //2删除user原来的角色
        rolePermissionRepository.delete(role);

        int result = 0;

        //3为user增加新的角色
        for (Permission permission : rolePermissionFormData.getPermissionList()) {
            String r = rolePermissionRepository.add(role.getId(), permission.getId());
            if (r != null) result += 1;
        }

        if (result > 0) logsService.updateLog(rolePermissionFormData);

        return result;
    }

    @Override
    public int update(String id, String roleId, String permissionId) {
        return rolePermissionRepository.update(id, roleId, permissionId);
    }

    @Override
    public boolean isExistById(String id) {
        return rolePermissionRepository.isExistById(id);
    }

    @Override
    public boolean isExist(String roleId, String permissionId) {
        return rolePermissionRepository.isExist(roleId, permissionId);
    }

    @Override
    public int getCount() {
        return rolePermissionRepository.getCount();
    }

    @Override
    public int getCountByPermissionId(String permissionId) {
        return rolePermissionRepository.getCountByPermissionId(permissionId);
    }

    @Override
    public int getCountByRoleId(String roleId) {
        return rolePermissionRepository.getCountByRoleId(roleId);
    }

    @Override
    public RolePermission getById(String id) {
        return rolePermissionRepository.getById(id);
    }

    @Override
    public List<RolePermission> getAll() {
        return rolePermissionRepository.getAll();
    }

    @Override
    public List<RolePermission> getByPermissionId(String permissionId) {
        return rolePermissionRepository.getByPermissionId(permissionId);
    }

    @Override
    public List<RolePermission> getByRoleId(String roleId) {
        return rolePermissionRepository.getByRoleId(roleId);
    }

    @Override
    public Page<RolePermission> getPage(int pageNo, int pageSize) {
        return rolePermissionRepository.getPage(pageNo, pageSize);
    }

    @Override
    public List<Role> getRolesByPermissionId(String permissionId) {
        List<RolePermission> rolePermissionList = getByPermissionId(permissionId);
        if (rolePermissionList == null || rolePermissionList.size() == 0) return null;

        List<Role> roleList = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissionList) {
            Role role = roleService.getById(rolePermission.getRoleId());
            if (role != null) roleList.add(role);
        }
        return roleList;
    }

    @Override
    public List<Permission> getPermissionsByRoleId(String roleId) {
        List<RolePermission> rolePermissionList = getByRoleId(roleId);
        if (rolePermissionList == null || rolePermissionList.size() == 0) return null;

        List<Permission> permissionList = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissionList) {
            Permission permission = permissionService.getById(rolePermission.getPermissionId());
            if (permission != null) permissionList.add(permission);
        }
        return permissionList;
    }
}
