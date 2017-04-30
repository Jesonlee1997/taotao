package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断拦截器是否
 * Created by JesonLee
 * on 2017/4/29.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @Value("${SSO_URL}")
    private String SSO_URL;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //执行handler之前先执行该方法
        //1、从Cookie中取Token信息
        String token = CookieUtils.getCookieValue(httpServletRequest, TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            //取当前请求的url
            String requestURL = httpServletRequest.getRequestURL().toString();
            httpServletResponse.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            //拦截
            return false;
        }
        //取到token，调用SSO的服务判断用户是否登录
        TaotaoResult result = userService.getUserByToken(token);
        if (result.getStatus() != 200) {
            //取当前请求的url
            String requestURL = httpServletRequest.getRequestURL().toString();
            httpServletResponse.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            //拦截
            return false;
        }
        //5、如果取到用户信息，放行
        //把用户信息放在request中
        TbUser user = (TbUser) result.getData();
        httpServletRequest.setAttribute("user", user);
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
