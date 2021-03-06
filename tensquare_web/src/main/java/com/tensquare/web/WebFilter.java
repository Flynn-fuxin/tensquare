package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    @Override
    public String filterType() {
        //过滤器类型
        //在请求到达网关的时候，网关路由执行之前执行
        // 前置过滤器
        //        return "pre";
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
         //过滤器是一个链条，也称之为过滤器栈，执行顺序是按照该方法的返回值判断的
        return 0;// 优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        //当前过滤器是否启用的开关，如果是true，则启用，false代表不启用。
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("web前台网关，经过了zuul前置过滤器...");
        //获取Request上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取Request对象
        HttpServletRequest request = requestContext.getRequest();
        //获取授权的header信息
        String authorizationHeader = request.getHeader("JwtAuthorization");
        String authorizationHeader2 = request.getHeader("Authorization");
        System.out.println("JwtAuthorization："+authorizationHeader);
        System.out.println("Authorization："+authorizationHeader2);
        //判断是否有该头信息
        if(null !=authorizationHeader2){
            //补充：可以判断请求uri
            String requestURI = request.getRequestURI();
            //打印网关的转发的uri：/qa/problem
            System.out.println("------------"+requestURI);
            if (requestURI.startsWith("/qa/")){
                //如果有，则转发头信息
                requestContext.addZuulRequestHeader("JwtAuthorization",authorizationHeader2);
            }
        }
        return null;
    }
}
