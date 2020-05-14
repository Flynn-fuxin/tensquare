package com.tensquare.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import constants.StatusCode;
import dto.ResultDTO;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerZuulFilter extends ZuulFilter {

    @Autowired
    protected JwtUtil jwtUtil;

    //只要在SpringBoot的web环境下.直接注入Jackson
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("经过了后台管理网站的网关过滤器");
        //目标：必须是管理员身份的请求才能进行转发
        //1.获取请求过来的权限头信息
        RequestContext requestContext = RequestContext.getCurrentContext();
        // Map<String, String> zuulRequestHeaders = requestContext.getZuulRequestHeaders();
        // String authorizationHeader = zuulRequestHeaders.get("Authorization");
        // System.out.println("--"+authorizationHeader);
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        //2. 其他请求处理
        //1）处理CORS跨域请求，因为跨域请求的第一次请求是预请求，不带头信息的，因此要过滤掉。
        if (request.getMethod().equals("OPTIONS")){
            System.out.println("跨域的第一次请求，直接放行");
            // HttpServletResponse response=requestContext.getResponse();
            // response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            // response.setHeader("Access-Control-Max-Age", "3600");
            // response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");//Access-Control-Allow-Headers配置允许前端提交的请求头信息
            // response.addHeader("Access-Control-Expose-Headers", "GID,SID");//Access-Control-Expose-Headers配置允许前端获取到的请求头信息
            //不是管理员身份，不再继续向下转发请求了
            //不转发请求到具体微服务上，直接响应给客户端
            //requestContext.setSendZuulResponse(false);
            return null;
        }
        //2）处理一些特殊请求，比如登录请求，就直接放行
        String url = request.getRequestURL().toString();
        if (url.indexOf("/admin/login")>0){
            System.out.println("管理员的登录请求，直接放行："+url);
            return null;
        }
        //3.判断头信息是否为空，是否满足格式，解析，判断
        //判断authorizationHeader不为空,并且是"Bearer "开头的
        if (null != authorization && authorization.startsWith("Bearer ")){
            //获取令牌，The part after "Bearer "
            final String token = authorization.substring(7);
            //获取载荷
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //判断载荷是否为空
            if (null != claims && "admin".equals(claims.get("roles"))) {
                //改名并转发下去
                requestContext.addZuulRequestHeader("JwtAuthorization", authorization);
                //放行
                return null;
            }
        }
        //不是管理员身份，不再继续向下转发请求了
        //不转发请求到具体微服务上，直接响应给客户端
        requestContext.setSendZuulResponse(false);
        //友好提示
        //普通文本
        //requestContext.getResponse().setContentType("text/html;charset=UTF-8");
        //requestContext.setResponseBody("您无权访问");
        //json响应
        requestContext.getResponse().setContentType("application/json;charset=UTF-8");
        //requestContext.setResponseBody("{\"code\": 20003,\"flag\": false,\"message\": \"您无权访问\"}");
        //定义对象转json
        ResultDTO resultDTO=new ResultDTO(false, StatusCode.ACCESSERROR,"您无权访问");
        String msg =null;
        try {
            msg = objectMapper.writeValueAsString(resultDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //写入响应
        requestContext.setResponseBody(msg);

        //http响应码401(没有授权)或403（禁止访问）
        //http状态码401，403，407
        //requestContext.setResponseStatusCode(401);
        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        return null;
    }
}
