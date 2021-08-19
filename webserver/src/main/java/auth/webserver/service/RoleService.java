package auth.webserver.service;


import auth.webserver.model.Page;
import auth.webserver.model.roles.Role;
import auth.webserver.model.roles.RoleView;

import java.util.List;

public interface RoleService {
    String add(Role role);

    String add(String name);

    int deleteById(String id);

    int delete(Role role);

    int deleteAll();

    int update(Role role);

    int update(String id, String name);


    boolean isExistById(String id);

    boolean isExistByName(String name);

    int getCount();

    int getLikeNameCount(String name);

    Role getById(String id);

    List<Role> getByLikeName(String name);

    List<Role> getAll();

    Page<Role> getPage(int pageNo, int pageSize);

    Page<Role> getSearchedPage(String searchText, Integer pageNo, Integer pageSize);

    List<RoleView> getAllRoleViews();

    Page<RoleView> getViewPage(int pageNo, int pageSize);


}
