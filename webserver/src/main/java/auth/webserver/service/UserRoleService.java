package auth.webserver.service;

import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.user.*;

import java.util.List;

public interface UserRoleService {
    String add(UserRole userRole);

    String add(List<UserRole> userRoleList);

    String add(UserRoleFormData userRoleFormData);

    String add(String userId, String roleId);

    int deleteById(String id);



    int delete(String userId, String roleId);

    int delete(UserRole userRole);

    int delete(UserRoleFormData userRoleFormData);

    int delete(User user);

    int deleteAll();

    int update(UserRole userRole);

    int update(UserRoleFormData userRoleFormData);

    int update(String id, String userId, String roleId);


    boolean isExistById(String id);

    boolean isExist(String userId, String roleId);

    boolean isExistPermission(String userId, String permissionId);

    boolean isExistPermission(UserPermissionFormData userPermissionFormData);

    int getCount();

    int getCountByUserId(String userId);

    int getCountByRoleId(String roleId);

    UserRole getById(String id);


    List<UserRole> getAll();

    List<UserRole> getByUserId(String userId);

    List<UserRole> getByRoleId(String roleId);

    Page<UserRole> getPage(int pageNo, int pageSize);

    Page<UserRoleView> getUserRoleViewPage(Integer pageNo, Integer pageSize);

    Page<UserRoleView> getUserRoleViewPage(String searchText, Integer pageNo, Integer pageSize);


    List<Role> getRolesByUserId(String userId);

    List<User> getUsersByRoleId(String roleId);

    Page<User> getUsersByRoleId(String roleId, Integer pageNo, Integer pageSize);

    Page<User> getUsersByRoleId(String searchText, String roleId, Integer pageNo, Integer pageSize);


}
