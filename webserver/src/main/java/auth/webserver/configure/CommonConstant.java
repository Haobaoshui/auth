package auth.webserver.configure;

/**
 * 整个应用通用的常量 <br>
 * <b>类描述:</b>
 *
 * <pre>
 * |
 * </pre>
 *
 * @see
 * @since
 */
public class CommonConstant {
    /**
     * 用户对象放到Session中的键名称
     */
    public static final String USER_CONTEXT = "USER_CONTEXT";
    public static final String USER_IDENTIFY_CODE = "USER_IDENTIFY_CODE";
    /**
     *
     * 每页的记录数
     */
    public static final int PAGE_SIZE = 10;
    public static final String LAST_ACCESS_TIME = "LastAccessTime";
    public static final String ACCESS_COUNT = "AccessCount";
    public static final int FREQUENT_ACCESS_WAITING_TIME = 30;//频繁访问需要等待30秒


    /**
     * 上传项目文件路径
     */
    //初版改成绝对路径，后续加到配置文件中
    public static final String PROJECT_UPLOAD_FILES_PATH = "static/project/uploadfiles/";
    /**
     * 模板文件处理
     * 上传模板文件路径
     */
    public static final String PROJECT_UPLOAD_MODELS_PATH = "static/project/uploadmodels/";


    //检测状态
    public static final int DETECT_STATUS_UNDETECTED = 0;//未检测
    public static final int DETECT_STATUS_SUCCESS = 1;//检测成功
    public static final int DETECT_STATUS_FAILED = 2;//检测失败
    public static final int DETECT_STATUS_DETECTING = 3;//正在检测
    public static final int DETECT_STATUS_PART = 4;//部分检测

    public static final String DETECT_STATUS_DESCRIPTION_UNDETECTED = "未检测";
    public static final String DETECT_STATUS_DESCRIPTION_SUCCESS = "检测完成";
    public static final String DETECT_STATUS_DESCRIPTION_FAILED = "检测失败";
    public static final String DETECT_STATUS_DESCRIPTION_DETECTING = "正在检测";
    public static final String DETECT_STATUS_DESCRIPTION_PART= "部分检测";

}