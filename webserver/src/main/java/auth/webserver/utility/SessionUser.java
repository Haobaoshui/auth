package auth.webserver.utility;

import auth.webserver.configure.CommonConstant;
import auth.webserver.model.user.UserSessionInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

public class SessionUser {

    /**
     * 得到操作用户的IP地址
     *
     * @return
     */
    private static String getLogIp() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) return null;

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();


        return ip;
    }

    public static void setSessionUser() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) return;

        request.getSession().removeAttribute(CommonConstant.USER_CONTEXT);
    }

    /**
     * 获取保存在Session中的用户对象
     *
     * @return
     */
    public static UserSessionInfo getSessionUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) return null;

        return (UserSessionInfo) request.getSession().getAttribute(
                CommonConstant.USER_CONTEXT);
    }

    /**
     * 保存用户对象到Session中
     *
     * @param user
     */
    public static void setSessionUser(UserSessionInfo user) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) return;

        user.setLogIP(SessionUser.getLogIp());


        request.getSession().setAttribute(CommonConstant.USER_CONTEXT, user);
    }

    /**
     * 产生验证码，这里仅产生4个数字
     *
     * @return
     */
    public static String genCode() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) return null;


        Random r = new Random();
        String code = "";
        for (int i = 0; i < 4; i++) code += r.nextInt(10);
        request.getSession().setAttribute(CommonConstant.USER_IDENTIFY_CODE, code);


        return code;

    }

    public static String getCode() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) return null;

        if (request.getSession() == null) return null;

        if (request.getSession().getAttribute(CommonConstant.USER_IDENTIFY_CODE) == null) return null;
        return request.getSession().getAttribute(CommonConstant.USER_IDENTIFY_CODE).toString();

    }
}
