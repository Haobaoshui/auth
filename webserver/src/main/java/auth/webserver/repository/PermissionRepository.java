package auth.webserver.repository;



import auth.webserver.model.Page;
import auth.webserver.model.roles.Permission;

import java.util.List;

public interface PermissionRepository {
    String add(Permission permission);

    String add(String name);

    int deleteById(String id);

    int delete(Permission permission);

    int deleteAll();

    int update(Permission permission);

    int update(String id, String name);


    boolean isExistById(String id);

    boolean isExistByName(String name);

    int getCount();

    int getLikeNameCount(String name);

    Permission getById(String id);

    List<Permission> getByLikeName(String name);

    List<Permission> getAll();

    Page<Permission> getPage(int pageNo, int pageSize);
}

