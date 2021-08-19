package auth.webserver.repository;

import auth.webserver.model.Page;
import auth.webserver.model.user.User;


import java.util.List;

public interface UserRepository {

    String add(User user);

    String add(String emplNum, String emplName, String emplPlainPwd);

    int deleteById(String id);

    int deleteByEmplNum(String emplNum);

    int delete(User user);

    int deleteAll();

    int update(User user);

    int update(String id, String emplNum, String emplName, String emplPlainPwd);

    int update(String id, String emplNum, String emplName);

    int update(String id, String emplPlainPwd);

    boolean isExistById(String id);

    boolean isExistByEmplNum(String emplNum);

    int getCount();

    int getCountLike(String text);

    User getById(String id);

    User getByEmplNum(String emplNum);

    List<User> getAll();

    Page<User> getPage(int pageNo, int pageSize);


    Page<User> getSearchedPage(String searchText, Integer pageNo, Integer pageSize);
}
