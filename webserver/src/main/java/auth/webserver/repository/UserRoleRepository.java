package auth.webserver.repository;

import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.user.User;
import auth.webserver.model.user.UserRole;


import java.util.List;

public interface UserRoleRepository {

    String add(UserRole userRole);

    String add(String userId, String roleId);

    int deleteById(String id);

    int delete(String userId, String roleId);

    int delete(UserRole userRole);

    int delete(User user);

    int delete(Role role);

    int deleteAll();

    int update(UserRole userRole);

    int update(String id, String userId, String roleId);


    boolean isExistById(String id);

    boolean isExist(String userId, String roleId);

    boolean isExistPermission(String userId, String permissionId);

    int getCount();

    int getCountByUserId(String userId);

    int getCountByRoleId(String roleId);

    UserRole getById(String id);

    UserRole getByUserIdAndRoleId(String userId, String roleId);


    List<UserRole> getAll();

    List<UserRole> getByUserId(String userId);

    List<UserRole> getByRoleId(String roleId);

    List<String> getAllUserIds();


    Page<UserRole> getPage(int pageNo, int pageSize);

    Page<String> getUserIdsPage(int pageNo, int pageSize);

    Page<String> getUserIdsPage(String searchText, Integer pageNo, Integer pageSize);

    Page<String> getUserIdsPageByRoleId(String t_role_id, int pageNo, int pageSize);

    Page<String> getUserIdsPageByRoleId(String searchText, String t_role_id, int pageNo, int pageSize);
}
