package auth.webserver.configure;


import auth.webserver.model.user.UserSessionInfo;
import auth.webserver.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;


@WebListener//监听器注解
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    @Autowired
    LogsService logsService;

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {

    }

    /**
     * session被创建时触发
     *
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
       // DebugDump.printDebugInfo();

    }

    /**
     * session被销毁时触发,如下情况:1.主动调用session.invalidate(),2.session超时
     *
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        HttpSession session = se.getSession();

        //获得当前用户信息
        UserSessionInfo userinfo = (UserSessionInfo) session.getAttribute(CommonConstant.USER_CONTEXT);

        //记录日志
        logsService.logoutLog(userinfo);

        session.removeAttribute(CommonConstant.USER_CONTEXT);

    }
}
