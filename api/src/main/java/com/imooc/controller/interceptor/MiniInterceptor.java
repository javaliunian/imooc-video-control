package com.imooc.controller.interceptor;

import com.imooc.controller.BasicController;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author daile.sun
 * @date 2018/10/19
 */
public class MiniInterceptor extends BasicController implements HandlerInterceptor{

    @Autowired
    private RedisOperator redis;

    /**
     * 拦截请求，在controller调用之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        /**
         * 返回false：请求被拦截，返回
         * 返回true：放行
         */
        String userId = request.getHeader("userId");
        String userToken = request.getHeader("userToken");
        if(StringUtils.isNotBlank(userId)&& StringUtils.isNotBlank(userToken)){
            String uniqueToken=redis.get(USER_REDIS_SESSION+":"+userId);
            if(StringUtils.isBlank(uniqueToken)){
                System.out.println("信息过期，请登录...");
                returnErrorResponse(response, IMoocJSONResult.errorTokenMsg("信息过期，请登录..."));
                return false;
            } else {
                if(!uniqueToken.equals(userToken)){
                    System.out.println("账号在别的手机登录...");
                    returnErrorResponse(response, IMoocJSONResult.errorTokenMsg("账号在别的手机登录..."));
                    return false;
                }
            }
        }else{
            System.out.println("请登录...");
            returnErrorResponse(response, IMoocJSONResult.errorTokenMsg("请登录..."));
            return false;
        }

        return true;

    }

    /**
     * 请求controller 之后，渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }


    /**
     * 请求controller之后，渲染视图之后
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult result)
            throws IOException {
        OutputStream out=null;
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally{
            if(out!=null){
                out.close();
            }
        }
    }
}
