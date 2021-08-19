package auth.webserver.controller;



import auth.webserver.configure.CommonConstant;
import auth.webserver.model.Page;
import auth.webserver.model.log.LogsStatistics;
import auth.webserver.model.log.LogsView;
import auth.webserver.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("log/v1")
public class LogsController {

    private final LogsService logsService;


    @Autowired
    public LogsController(LogsService logsService) {
        this.logsService = logsService;
    }


    /**
     * @param pageNo
     * @param pageSize dateBegin: 2020-10-11T16:00:00.798Z
     *                 dateEnd: 2020-10-18T15:59:59.792Z
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public Page<LogsView> getPage(@RequestParam(value = "dateBegin", required = false) String dateBegin,
                                  @RequestParam(value = "dateEnd", required = false) String dateEnd,
                                  @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) throws ParseException {

//      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = null;
        if (!StringUtils.isEmpty(dateBegin)){
            beginDate = formatter.parse(dateBegin);
        }
        Date endDate = null;
        if (!StringUtils.isEmpty(dateBegin)){
            endDate = formatter.parse(dateEnd);
        }

        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return logsService.getPage(beginDate, endDate, pageNo, pageSize);


    }

    @RequestMapping(value = "searchpage", method = RequestMethod.GET)
    public Page<LogsView> getSearchPage(@RequestParam(value = "searchText", required = false) String searchText,
                                        @RequestParam(value = "dateBegin", required = false) String dateBegin,
                                        @RequestParam(value = "dateEnd", required = false) String dateEnd,
                                        @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) throws ParseException {

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = null;
        if (!StringUtils.isEmpty(dateBegin)) {
             beginDate = formatter.parse(dateBegin);
           }
         Date endDate = null;
        if (!StringUtils.isEmpty(dateEnd)) {
            endDate = formatter.parse(dateEnd);
        }
        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return logsService.getPage(searchText, beginDate, endDate, pageNo, pageSize);

    }

    @RequestMapping(value = "userpage", method = RequestMethod.GET)
    public Page<LogsView> getUserPage(@RequestParam(value = "userId", required = true) String userId,
                                      @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return logsService.getPage(userId, pageNo, pageSize);


    }

    @RequestMapping(value = "usersearchpage", method = RequestMethod.GET)
    public Page<LogsView> getUserSearchPage(@RequestParam(value = "userId", required = true) String userId,
                                            @RequestParam(value = "dateBegin", required = false) String dateBegin,
                                            @RequestParam(value = "dateEnd", required = false) String dateEnd,
                                            @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws ParseException {

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        if (!StringUtils.isEmpty(dateBegin)) {
            startDate = formatter.parse(dateBegin);
         }
        Date endDate = null;
        if (!StringUtils.isEmpty(dateEnd)) {
             endDate = formatter.parse(dateEnd);
         }

        pageNo = pageNo == null ? 1 : (pageNo < 1 ? 1 : pageNo);
        pageSize = pageSize == null ? CommonConstant.PAGE_SIZE : (pageNo < 1 ? 1 : pageSize);
        return logsService.getPageByUserId(userId, startDate, endDate, pageNo, pageSize);


        }

    @RequestMapping(value = "statistics", method = RequestMethod.GET)
    public List<LogsStatistics> getLogsStatisticsList(@RequestParam(value = "searchText", required = false) String searchText,
                                                      @RequestParam(value = "dateBegin", required = false) String beginDate,
                                                      @RequestParam(value = "dateEnd", required = false) String endDateStr) throws ParseException {

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        if (!StringUtils.isEmpty(beginDate)) {
            startDate = formatter.parse(beginDate);
        }
        Date endDate = null;
        if (!StringUtils.isEmpty(endDateStr)) {
            endDate = formatter.parse(endDateStr);
        }
        return logsService.getStatisticsList(searchText, startDate, endDate);
    }

//    public List<LogsStatistics> getLogsStatisticsList(@RequestParam(value = "searchText", required = false) String searchText,
//                                                      @RequestParam(value = "dateBegin", required = false) Date dateBegin,
//                                                      @RequestParam(value = "dateEnd", required = false) Date dateEnd) {
//        return logsService.getStatisticsList(searchText, dateBegin, dateEnd);
//    }

    @RequestMapping(value = "userstatistics", method = RequestMethod.GET)
    public List<LogsStatistics> getUserLogsStatisticsList(@RequestParam(value = "userId", required = true) String userId) {
        return logsService.getStatisticsList(userId);
    }

}
