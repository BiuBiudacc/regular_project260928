package com.stirwick.hotelmanagement.config;
import com.stirwick.hotelmanagement.enums.ResultCodeEnum;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.util.JwtUtil;
import com.stirwick.hotelmanagement.util.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    // 注入JWT工具
    @Resource
    private JwtUtil jwtUtil;
    /**
     * Controller执行之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1.从请求头获取 Authorization：前端约定格式：Bearer 空格 + token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            // 截掉前7个字符 "Bearer "，拿到真正token字符串
            token = authHeader.substring(7);
        }

        // 2.白名单判断：登录接口不需要token直接放行
        String uri = request.getRequestURI();
        boolean isWhiteList = uri.startsWith("/api/user/login")
                || uri.startsWith("/doc.html")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-ui/")
                || uri.startsWith("/webjars/");
        if(isWhiteList){
            return true;
        }

        // 3.非白名单，没有携带token
        if(token == null || token.isBlank()){
            throw new BusinessException(ResultCodeEnum.NOT_LOGIN);
        }

        // 4.校验token是否合法（过期/篡改）
        if(!jwtUtil.validateToken(token)){
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }

        // 5.解析token拿到userId，存入线程上下文
        Long userId = jwtUtil.getUserIdByToken(token);
        UserContextUtil.setUserId(userId);

        // 全部校验通过，放行进入Controller
        return true;
    }

    /**
     * 请求完全结束后执行，无论正常/异常都执行
     * 核心：清理ThreadLocal，防止线程池复用线程脏数据
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        UserContextUtil.clear();
    }
}
