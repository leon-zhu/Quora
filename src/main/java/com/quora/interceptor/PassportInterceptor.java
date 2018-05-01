package com.quora.interceptor;

import com.quora.dao.LoginTicketDAO;
import com.quora.dao.UserDAO;
import com.quora.module.HostHolder;
import com.quora.module.LoginTicket;
import com.quora.module.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 自定义拦截器
 * 每次请求若发现cookie中有ticket, 那么将对应用户加入到ThreadLocal中(preHandle).
 * 在渲染前, 将对应threadLocal中的对应用户加入到ModelAndView中(postHandle).
 *
 *
 * @author: leon
 * @date: 2018/5/1 20:06
 * @version: 1.0
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserDAO userDAO;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() == 1)
                return true;
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUsers(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        hostHolder.clear();
    }
}
