package com.recruiting.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        //过滤器类型
        //pre:之前过虑  post:之后过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        //当前过滤器的优先级
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //当前过滤器是否开启
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //过滤器内执行的操作
        //return 任意类型Object的值都表示继续执行
        //setSendZuulResponse(false) 不再继续执行
        //System.out.println("Zuul:经过当前过滤器了");
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //放行Zuul分配微服务的OPTIONS请求
        if (request.getMethod().equals("OPTIONS")){
            return null;
        }

        //放行登录请求
        if (request.getRequestURI().indexOf("/admin/login") > 0){
            return null;
        }

        String header = request.getHeader("Authorization");
        if (header != null &&  header.startsWith("Bearer ")){
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);
                String  roles = (String) claims.get("roles");
                if (roles.equals("admin")){
                    currentContext.addZuulRequestHeader("Authorization",header);
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseStatusCode(403);
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setCharacterEncoding("utf-8");
        return null;
    }
}
