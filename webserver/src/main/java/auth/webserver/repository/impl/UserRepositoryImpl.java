package auth.webserver.repository.impl;

import auth.webserver.model.Page;
import auth.webserver.utility.EncoderHandler;
import auth.webserver.utility.GUID;
import auth.webserver.model.user.User;
import auth.webserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String add(User user) {
        if (user == null) {
            return null;
        }

        return add(user.getEmplNum(), user.getEmplName(), user.getEmplPwd());
    }

    /**
     * 添加用户
     *
     * @param emplNum      工号
     * @param emplName     名字
     * @param emplPlainPwd 明文密码，需要MD5或SHA处理后存入数据库
     * @return
     */
    @Override
    public String add(String emplNum, String emplName, String emplPlainPwd) {
        String newid = GUID.getGUID();

        try{
            int res = jdbcTemplate.update("INSERT INTO t_user(id,empl_num,empl_name,empl_pwd) VALUES(?,?,?,?)", newid, emplNum, emplName, EncoderHandler.encode(emplPlainPwd));
        }catch (Exception e){
            e.getStackTrace();
            return "";
        }

        return "添加成功";
//        return newid;
    }

    @Override
    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE from  t_user where id=?", id);
    }

    @Override
    public int deleteByEmplNum(String emplNum) {
        return jdbcTemplate.update("DELETE from  t_user where empl_num=?", emplNum);
    }

    @Override
    public int delete(User user) {
        if (user == null || user.getId() == null) {
            return 0;
        }
        return deleteById(user.getId());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("truncate t_user");
    }

    @Override
    public int update(User user) {
        if (user == null || user.getId() == null) {
            return 0;
        }
        return update(user.getId(), user.getEmplNum(), user.getEmplName());
    }

    @Override
    public int update(String id, String emplNum, String emplName, String emplPlainPwd) {
        if (id == null) {
            return 0;
        }

        return jdbcTemplate.update("update t_user set empl_num=?,empl_name=?,empl_pwd=? WHERE id=?", emplNum, emplName, EncoderHandler.encode(emplPlainPwd), id);
    }

    @Override
    public int update(String id, String emplNum, String emplName) {
        if (id == null) {
            return 0;
        }

        return jdbcTemplate.update("update t_user set empl_num=?,empl_name=?  WHERE id=?", emplNum, emplName, id);

    }

    @Override
    public int update(String id, String emplPlainPwd) {
        if (id == null) {
            return 0;
        }

        return jdbcTemplate.update("update t_user set empl_pwd=? WHERE id=?", EncoderHandler.encode(emplPlainPwd), id);

    }

    @Override
    public boolean isExistById(String id) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_user WHERE id=?", new
                Object[]{id}, Integer.class) == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isExistByEmplNum(String emplNum) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_user WHERE empl_name=?", new
                Object[]{emplNum}, Integer.class) == 0) {
            return false;
        }
        return true;
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from t_user", Integer.class);
    }

    @Override
    public int getCountLike(String text) {
        text = "%" + text.trim() + "%";
        return jdbcTemplate.queryForObject("select count(*) from t_user where empl_name like ? or empl_num like ?", new Object[]{text, text}, Integer.class);
    }

    @Override
    public User getById(String id) {
        if (!isExistById(id)) {
            return null;
        }

        return jdbcTemplate.queryForObject("SELECT * FROM t_user WHERE id=?", new Object[]{id}, new UserMapper());
    }

    @Override
    public User getByEmplNum(String emplNum) {
        if (jdbcTemplate.queryForObject("SELECT count(*) FROM t_user WHERE empl_num=?", new
                Object[]{emplNum}, Integer.class) != 1) {
            return null;
        }

        return jdbcTemplate.queryForObject("SELECT * FROM t_user WHERE empl_num=?", new Object[]{emplNum}, new UserMapper());
    }

    @Override
    public List<User> getAll() {
        if (getCount() <= 0) {
            return null;
        }
        return jdbcTemplate.query("select * from t_user",
                new UserMapper());
    }

    private List<User> PageQuery(int PageBegin, int PageSize) {
        return jdbcTemplate.query("select * from t_user  limit ?,? ",
                new Object[]{PageBegin * PageSize, PageSize},
                new UserMapper());
    }

    @Override
    public Page<User> getPage(int pageNo, int pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCount();
        if (totalCount < 1) {
            return null;
        }


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<User> data = PageQuery(pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private List<User> PageQuery(String searchText, int PageBegin, int PageSize) {
        searchText = "%" + searchText.trim() + "%";
        return jdbcTemplate.query("select * from t_user  where empl_name like ? or empl_num like ? limit ?,? ",
                new Object[]{searchText, searchText, PageBegin * PageSize, PageSize},
                new UserMapper());
    }

    @Override
    public Page<User> getSearchedPage(String searchText, Integer pageNo, Integer pageSize) {
        //pageNo从1开始，而非从0开始
        long totalCount = getCountLike(searchText);
        if (totalCount < 1) {
            return new Page<>();
        }


        //实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);

        List<User> data = PageQuery(searchText, pageNo - 1, pageSize);

        return new Page<>(startIndex, totalCount, pageSize, data);
    }

    private static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setEmplName(rs.getString("empl_name"));
            user.setEmplNum(rs.getString("empl_num"));
            user.setEmplPwd(rs.getString("empl_pwd"));
            return user;
        }
    }
}
