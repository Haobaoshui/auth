package auth.webserver.service.impl;


import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;
import auth.webserver.model.user.UserSessionInfo;
import auth.webserver.repository.PermissionRepository;
import auth.webserver.service.LogsService;
import auth.webserver.service.PermissionService;
import auth.webserver.utility.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private LogsService logsService;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, LogsService logsService) {

        this.permissionRepository = permissionRepository;
        this.logsService = logsService;
    }

    @Override
    public String add(Permission permission) {

        UserSessionInfo userinfo = SessionUser.getSessionUser();
        if (userinfo == null) return null;

        if (!isExistByName(permission.getName())) return null;

        String id = permissionRepository.add(permission);


        if (id != null) {
            permission.setId(id);

            //日志:增加用户
            logsService.addLog(permission);

        }

        return id;


    }

    @Override
    public String add(String name) {
        return permissionRepository.add(name);
    }

    @Override
    public int deleteById(String id) {
        return permissionRepository.deleteById(id);
    }

    @Override
    public int delete(Permission permission) {
        return permissionRepository.delete(permission);
    }

    @Override
    public int deleteAll() {
        return permissionRepository.deleteAll();
    }

    @Override
    public int update(Permission permission) {
        return permissionRepository.update(permission);
    }

    @Override
    public int update(String id, String name) {
        return permissionRepository.update(id, name);
    }

    @Override
    public boolean isExistById(String id) {
        return permissionRepository.isExistById(id);
    }

    @Override
    public boolean isExistByName(String name) {
        return permissionRepository.isExistByName(name);
    }

    @Override
    public int getCount() {
        return permissionRepository.getCount();
    }

    @Override
    public int getLikeNameCount(String name) {
        return permissionRepository.getLikeNameCount(name);
    }

    @Override
    public Permission getById(String id) {
        return permissionRepository.getById(id);
    }

    @Override
    public List<Permission> getByLikeName(String name) {
        return permissionRepository.getByLikeName(name);
    }

    @Override
    public List<Permission> getAll() {
        return permissionRepository.getAll();
    }

    @Override
    public Page<Permission> getPage(int pageNo, int pageSize) {
        return permissionRepository.getPage(pageNo, pageSize);
    }
}
