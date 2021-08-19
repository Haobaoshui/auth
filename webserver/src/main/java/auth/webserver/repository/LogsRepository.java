package auth.webserver.repository;



import auth.webserver.model.Page;
import auth.webserver.model.log.Logs;
import auth.webserver.model.user.User;

import java.util.Date;
import java.util.List;

public interface LogsRepository {

    String add(Logs logs);

    String add(String userId, String logIp, int logType, String logContent);

    int deleteById(String id);

    int deleteByUserId(String userId);

    int delete(Logs logs);

    int delete(User user);

    int deleteAll();

    int update(Logs logs);

    int update(String id, String userId, String logIp, int logType, String logContent);

    boolean isExistById(String id);

    int getCount();

    int getCount(String searchText, Date dateBegin, Date dateEnd);

    int getCountByUserId(String userId);


    int getCountByUserId(String userId, Date dateBegin, Date dateEnd);

    int getCountByLogType(int log_type);

    int getCountByLogType(String userId, int log_type);


    Logs getById(String id);


    List<Logs> getAll();

    List<Integer> getAllLogTypes();

    List<Integer> getAllLogTypesUserLike(String userSearchText);

    List<Integer> getAllLogTypesUserLike(String userSearchText, Date dateBegin, Date dateEnd);

    List<Integer> getAllLogTypes(String userId);

    List<Integer> getAllLogTypes(String userId, Date dateBegin, Date dateEnd);

    Page<Logs> getPage(Date dateBeginDate, Date dateEndDate, int pageNo, int pageSize);

    Page<Logs> getPage(String userId, int pageNo, int pageSize);

    Page<Logs> getPage(String searchText, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize);

    Page<Logs> getPageByUserId(String userId, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize);


}
