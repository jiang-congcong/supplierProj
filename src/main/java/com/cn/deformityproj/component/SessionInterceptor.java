package com.cn.deformityproj.component;

import com.cn.deformityproj.config.RedisConfig;
import com.cn.deformityproj.utils.CommonUtils;
import com.cn.deformityproj.utils.RedisOperationUtils;
import com.xiaoleilu.hutool.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiangcongcong
 * @date 2022/2/23 16:00
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    public static Logger log = LoggerFactory.getLogger(SessionInterceptor.class);

    @Autowired
    private RedisOperationUtils redisOperationUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("=============呜呜啦啦，进入拦截器了=================================");
        String authorization = request.getHeader("Authorization");
         /* if (StringUtils.isBlank(authorization)) {
            authorization = CookieUtils.getCookieValue(request, "QZH_TOKEN");
        }*/
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");

        response.setHeader("Access-Control-Max-Age", "3600");

        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setHeader("Access-Control-Allow-Headers", "*");

        response.setHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Content-Type", "application/json");

        if(StringUtils.isEmpty(authorization)){
            log.info("调用接口拦截验证登陆信息不存在，说明状态为未登陆！");
            return false;
        }else{
            if(!redisOperationUtils.hasKey(authorization)){ //redis中不存在
                log.info("登陆信息过期，说明状态为未登陆！");
                return false;
            }
            return true;
        }
    }
    //请求处理后，渲染ModelAndView前调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("=============呜呜啦啦，进入拦截器了,请求处理后，渲染ModelAndView前调用。=================================");
    }
    //渲染ModelAndView后调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("=============呜呜啦啦，进入拦截器了,渲染ModelAndView后调用。=================================");
    }

}
