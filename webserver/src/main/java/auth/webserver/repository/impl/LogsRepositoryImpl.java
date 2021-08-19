package auth.webserver.repository.impl;

import auth.webserver.model.Page;
import auth.webserver.model.log.Logs;
import auth.webserver.model.user.User;
import auth.webserver.repository.LogsRepository;
import auth.webserver.utility.GUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class LogsRepositoryImpl implements LogsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LogsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String add(Logs logs) {
        if (logs == null) return null;
        return add(logs.getUserId(), logs.getLogIp(), logs.getLogType(), logs.getLogContent());
    }

    @Override
    public String add(String userId, String logIp, int logType, String logContent) {
        String newid = GUID.getGUID();

        jdbcTemplate.update("INSERT INTO t_logs(id,t_user_id,log_ip,log_type,log_content,log_time) VALUES(?,?,?,?,?,?)", newid,
                userId, logIp, logType, logContent, new Date());
        return newid;
    }

    @Override
    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE from  t_logs where id=?", id);
    }

    @Override
    public int deleteByUserId(String userId) {
        return jdbcTemplate.update("DELETE from  t_logs where t_user_id=?", userId);
    }

    @Override
    public int delete(Logs logs) {
        if (logs == null || logs.getId() == null) return 0;
        return deleteById(logs.getId());
    }

    @Override
    public int delete(User user) {
        if (user == null || user.getId() == null) return 0;
        return deleteByUserId(user.getId());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("truncate t_logs");
    }

    @Override
    public int update(Logs logs) {
        if (logs == null || logs.getId() == null) return 0;
        return update(logs.getId(), logs.getUserId(), logs.getLogIp(), logs.getLogType(), logs.getLogContent());
    }

    @Override
    public int update(String id, String userId, String logIp, int logType, String logContent) {
        if (id == null) return 0;

        return jdbcTemplate.update("update t_logs set t_user_id=?,log_ip=?,log_type=?,log_content=?  WHERE id=?",
                userId, logIp, logType, logContent, id);
    }

    @Override
    public boolean isExistById(String id) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_logs WHERE id=?", new
                Object[]{id}, Integer.class) == 0) return false;
        return true;
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from t_logs", Integer.class);
    }

    @Override
    public int getCount(String searchText, Date dateBegin, Date dateEnd) {
        if (searchText == null && dateBegin == null && dateEnd == null) return 0;
        if (searchText == null)
            return jdbcTemplate.queryForObject("select count(*) from t_logs where Date(log_time) between (?) and (?)", new Object[]{dateBegin, dateEnd}, Integer.class);


        searchText = "%" + searchText + "%";
        if (dateBegin == null)
            return jdbcTemplate.queryForObject("select count(*) from t_logs" +
                                   " left  join t_user on t_user.id=t_logs.t_user_id " +
                                    "where t_user.empl_name like ? or t_user.empl_num like ?"
                                    , new Object[]{searchText, searchText}, Integer.class);
        else{
            return jdbcTemplate.queryForObject("select count(*) from t_logs" +
                                        " left  join t_user on t_user.id=t_logs.t_user_id " +
                                        "where (t_user.empl_name like ? or t_user.empl_num like ?) " +
                                        "and Date(log_time) between (?) and (?)", new Object[]{searchText, searchText, dateBegin, dateEnd}, Integer.class);
        }

    }

//    public int getCount(String searchText, Date dateBegin, Date dateEnd) {
//        if (searchText == null && dateBegin == null && dateEnd == null) return 0;
//        if (searchText == null)
//            return jdbcTemplate.queryForObject("select count(*) from t_logs where log_time>=? and log_time<=?", new Object[]{dateBegin, dateEnd}, Integer.class);
//
//        searchText = "%" + searchText + "%";
//        if (dateBegin == null)
//            return jdbcTemplate.queryForObject("select count(*) from t_logs left  join t_user on t_user.id=t_logs.t_user_id where t_user.empl_name like ? or t_user.empl_num like ?", new Object[]{searchText, searchText}, Integer.class);
//        else
//            return jdbcTemplate.queryForObject("select count(*) from t_logs left  join t_user on t_user.id=t_logs.t_user_id where (t_user.empl_name like ? or t_user.empl_num like ?) and log_time>=? and log_time<=?", new Object[]{searchText, searchText, dateBegin, dateEnd}, Integer.class);
//    }

    @Override
    public int getCountByUserId(String userId) {
        return jdbcTemplate.queryForObject("select count(*) from t_logs where t_user_id=?", new Object[]{userId}, Integer.class);


    }


    @Override
    public int getCountByUserId(String userId, Date dateBegin, Date dateEnd) {
        if (dateBegin == null && dateEnd == null) return 0;


        return jdbcTemplate.queryForObject("select count(*) from t_logs where t_user_id=? and log_time>=? and log_time<=? ", new Object[]{userId, dateBegin, dateEnd}, Integer.class);

    }

    @Override
    public int getCountByLogType(int log_type) {
        return jdbcTemplate.queryForObject("select count(*) from t_logs where log_type=?", new Object[]{log_type}, Integer.class);
    }

    @Override
    public int getCountByLogType(String userId, int log_type) {
        return jdbcTemplate.queryForObject("select count(*) from t_logs where t_user_id=? and log_type=?", new Object[]{userId, log_type}, Integer.class);
    }

    @Override
    public Logs getById(String id) {
        if (!isExistById(id)) return null;


        return jdbcTemplate.queryForObject("SELECT * FROM t_logs WHERE id=?", new Object[]{id}, new LogsMapper());
    }

    @Override
    public List<Logs> getAll() {
        if (getCount() <= 0) return null;
        return jdbcTemplate.query("select * from t_logs",
                new LogsMapper());
    }

    @Override
    public List<Integer> getAllLogTypes() {
        if (getCount() <= 0) return null;
        return jdbcTemplate.queryForList("select distinct(log_type) from t_logs", Integer.class);
    }

    @Override
    public List<Integer> getAllLogTypesUserLike(String userSearchText) {

        if (getCount(userSearchText, null, null) <= 0) return null;

        userSearchText = "%" + userSearchText + "%";

        return jdbcTemplate.queryForList("select distinct(log_type) from t_logs left  join t_user on t_user.id=t_logs.t_user_id where t_user.empl_name like ? or t_user.empl_num like ?", new Object[]{userSearchText}, Integer.class);
    }

    @Override
    public List<Integer> getAllLogTypesUserLike(String userSearchText, Date dateBegin, Date dateEnd) {
        if (getCount(userSearchText, dateBegin, dateEnd) <= 0) return null;

        if (userSearchText == null)
            return jdbcTemplate.queryForList("select distinct(log_type) from t_logs where log_time>=? and log_time<=?", new Object[]{dateBegin, dateEnd}, Integer.class);

        userSearchText = "%" + userSearchText + "%";
        if (dateBegin == null)
            return jdbcTemplate.queryForList("select distinct(log_type) from t_logs left  join t_user on t_user.id=t_logs.t_user_id where t_user.empl_name like ? or t_user.empl_num like ?", new Object[]{userSearchText, userSearchText}, Integer.class);
        else
            return jdbcTemplate.queryForList("select distinct(log_type) from t_logs left  join t_user on t_user.id=t_logs.t_user_id where (t_user.empl_name like ? or t_user.empl_num like ?) and log_time>=? and log_time<=?", new Object[]{userSearchText, userSearchText, dateBegin, dateEnd}, Integer.class);


    }

    @Override
    public List<Integer> getAllLogTypes(String userId) {
        if (getCountByUserId(userId) <= 0) return null;

        return jdbcTemplate.queryForList("select distinct(log_type) from t_logs where t_user_id=?", new Object[]{userId}, Integer.class);
    }

    @Override
    public List<Integer> getAllLogTypes(String userId, Date dateBegin, Date dateEnd) {
        if (getCountByUserId(userId, dateBegin, dateEnd) <= 0) return null;

        return jdbcTemplate.queryForList("select count(*) from t_logs where t_user_id=? and log_time>=? and log_time<=? ", new Object[]{userId, dateBegin, dateEnd}, Integer.class);
    }

    @Override
    public Page<Logs> getPage(Date beginDate, Date endDate, int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount();
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        int PageBegin = pageNo - 1;
        int PageSize = pageSize;

        List<Logs> data = null;
        if (beginDate!=null && endDate!=null){
            data = jdbcTemplate.query("select * \n" +
                                     "from t_logs \n" +
                                    "where Date(log_time) between (?) and (?) order by log_time desc limit ?,? ",
            new Object[]{beginDate, endDate, PageBegin * PageSize, PageSize},
            new LogsMapper());
          totalCount = getCount(null,beginDate,endDate);
        }else {
            data = jdbcTemplate.query("select * from t_logs " +
                            "order by log_time desc limit ?,? ",
                    new Object[]{PageBegin * PageSize, PageSize},
                    new LogsMapper());
           totalCount=getCount();
        }
        return new Page<>(startIndex, totalCount, pageSize, data);
//        List<Logs> data = null;
//        if (beginDate!=null && endDate!=null){
//            data = jdbcTemplate.query("select * from t_logs " +
//                            "where log_time>=? and log_time<=? order by log_time desc limit ?,? ",
//                    new Object[]{beginDate, endDate, PageBegin * PageSize, PageSize},
//                    new LogsMapper());
//        }else {
//            data = jdbcTemplate.query("select * from t_logs " +
//                            "order by log_time desc limit ?,? ",
//                    new Object[]{PageBegin * PageSize, PageSize},
//                    new LogsMapper());
//        }
    }

    private List<Logs> PageQuery(String userId, int PageBegin, int PageSize) {
        return jdbcTemplate.query("select * from t_logs where t_user_id=? order by log_time desc limit ?,? ",
                new Object[]{userId, PageBegin * PageSize, PageSize},
                new LogsMapper());
    }

    @Override
    public Page<Logs> getPage(String userId, int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCountByUserId(userId);
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<Logs> data = PageQuery(userId, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private List<Logs> PageQuery(String searchText, Date dateBegin, Date dateEnd, int PageBegin, int PageSize) {
        if (searchText == null)
           return jdbcTemplate.query("select * \n" +
                                                  "from t_logs \n" +
                           "where Date(log_time) between (?) and (?) order by log_time desc limit ?,? ",
                   new Object[]{dateBegin, dateEnd, PageBegin * PageSize, PageSize}, new LogsMapper());

        searchText = "%" + searchText + "%";
        if (dateBegin == null && dateEnd == null){
            return jdbcTemplate.query("select * from t_logs left  join t_user on t_user.id=t_logs.t_user_id where t_user.empl_name like ? or t_user.empl_num like ? order by log_time desc limit ?,? ",
                    new Object[]{searchText, searchText, PageBegin * PageSize, PageSize}, new LogsMapper());
        }
        else{
            return jdbcTemplate.query("select * from t_logs left  join t_user on t_user.id=t_logs.t_user_id where (t_user.empl_name like ? or t_user.empl_num like ?) and Date(log_time) between (?) and (?) order by log_time desc limit ?,? ",
                  new Object[]{searchText, searchText, dateBegin, dateEnd, PageBegin * PageSize, PageSize}, new LogsMapper());
        }

    }


//    private List<Logs> PageQuery(String searchText, Date dateBegin, Date dateEnd, int PageBegin, int PageSize) {
//
//        if (searchText == null)
//            return jdbcTemplate.query("select * from t_logs where log_time>=? and log_time<=? order by log_time desc limit ?,? ",
//                    new Object[]{dateBegin, dateEnd, PageBegin * PageSize, PageSize}, new LogsMapper());
//
//        searchText = "%" + searchText + "%";
//        if (dateBegin == null)
//            return jdbcTemplate.query("select * from t_logs left  join t_user on t_user.id=t_logs.t_user_id where t_user.empl_name like ? or t_user.empl_num like ? order by log_time desc limit ?,? ",
//                    new Object[]{searchText, searchText, PageBegin * PageSize, PageSize}, new LogsMapper());
//        else
//            return jdbcTemplate.query("select * from t_logs left  join t_user on t_user.id=t_logs.t_user_id where (t_user.empl_name like ? or t_user.empl_num like ?) and log_time>=? and log_time<=? order by log_time desc limit ?,? ",
//                    new Object[]{searchText, searchText, dateBegin, dateEnd, PageBegin * PageSize, PageSize}, new LogsMapper());
//
//
//    }

    @Override
    public Page<Logs> getPage(String searchText, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount(searchText, dateBegin, dateEnd);
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<Logs> data = PageQuery(searchText, dateBegin, dateEnd, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private List<Logs> PageQueryByUserId(String userId, Date dateBegin, Date dateEnd, int PageBegin, int PageSize) {


        if (dateBegin == null)
            return jdbcTemplate.query("select * from t_logs left  join t_user on t_user.id=t_logs.t_user_id where t_user_id=?   order by log_time desc limit ?,?",
                    new Object[]{userId, PageBegin * PageSize, PageSize}, new LogsMapper());
        else
            return jdbcTemplate.query("select * from t_logs left  join t_user on t_user.id=t_logs.t_user_id where t_user_id=?  and log_time>=? and log_time<=? order by log_time desc limit ?,?",
                    new Object[]{userId, dateBegin, dateEnd, PageBegin * PageSize, PageSize}, new LogsMapper());


    }

    @Override
    public Page<Logs> getPageByUserId(String userId, Date dateBegin, Date dateEnd, Integer pageNo, Integer pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCountByUserId(userId, dateBegin, dateEnd);
        if (totalCount < 1) return new Page<>();


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<Logs> data = PageQueryByUserId(userId, dateBegin, dateEnd, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private static final class LogsMapper implements RowMapper<Logs> {

        @Override
        public Logs mapRow(ResultSet rs, int rowNum) throws SQLException {
            Logs logs = new Logs();
            logs.setId(rs.getString("id"));
            logs.setUserId(rs.getString("t_user_id"));
            logs.setLogIp(rs.getString("log_ip"));
            logs.setLogTime(rs.getTimestamp("log_time"));
            logs.setLogType(rs.getInt("log_type"));
            logs.setLogContent(rs.getString("log_content"));


            return logs;
        }
    }
}
