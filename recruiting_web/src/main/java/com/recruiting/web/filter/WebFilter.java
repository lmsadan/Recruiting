package com.recruiting.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
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
        //得到request上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = currentContext.getRequest();
        //得到头信息
        String header = request.getHeader("Authorization");
        //判断是否有特定头信息
        if (header != null && !"".equals(header)){
            //把请求头信息继续向下传
            currentContext.addZuulRequestHeader("Authorization",header);
        }
        System.out.println("经过网关过滤器");
        return null;
    }
}
