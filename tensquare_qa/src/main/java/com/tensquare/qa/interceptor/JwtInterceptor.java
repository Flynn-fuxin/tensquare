package com.tensquare.qa.interceptor;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private long begin;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        begin = System.currentTimeMillis();
        System.out.println("经过了JwtInterceptor拦截器...");
        String jwtAuthorization = request.getHeader("JwtAuthorization");

        final String authorizationHeader2 = request.getHeader("Authorization");
        System.out.println("------------------------");
        System.out.println("JwtAuthorization："+jwtAuthorization);
        System.out.println("Authorization："+authorizationHeader2);
        //判断authorizationHeader不为空,并且是"Bearer "开头的
        if (null != jwtAuthorization || jwtAuthorization.startsWith("Bearer ")){
            String token = jwtAuthorization.substring(7);
            Claims claims=null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //判断载荷是否为空
            if (null != claims){
                //判断令牌中的自定义载荷中的角色是否是admin（管理员）
                if("admin".equals(claims.get("roles"))){
                    request.setAttribute("admin_claims",claims);
                }
                //判断令牌中的自定义载荷中的角色是否是user（普通用户）
                if("user".equals(claims.get("roles"))) {
                    request.setAttribute("user_claims", claims);
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("所用时间为："+ (System.currentTimeMillis()-begin));
    }
}
