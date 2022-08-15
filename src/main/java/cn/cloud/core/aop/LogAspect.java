package cn.cloud.core.aop;


import cn.cloud.entity.AdminLog;
import cn.cloud.jwt.JwtUser;
import cn.cloud.mapper.AdminLogMapper;
import cn.cloud.service.LoginService;
import io.github.xfuns.java.Fun;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 请求日志AOP
 *
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminLogMapper adminLogMapper;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * cn.cloud.controller.api..*.*(..)) " +
            "&& !execution(public * cn.cloud.controller.api.AdminLogApiController.*(..)) " +
            "&& !execution(public * cn.cloud.controller.api.LoginApiController.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(returning = "ret", pointcut = "log()")
    public void doAfter(Object ret) {
        long spendTime = System.currentTimeMillis() - startTime.get();

        String requestUri = Fun.trimToDefault(httpRequest.getRequestURI(), "");
        String queryString = Fun.trimToDefault(httpRequest.getQueryString(), "");
        String url = Fun.empty(queryString) ? requestUri : requestUri + "?" + queryString;
        String method = Fun.trimToDefault(httpRequest.getMethod(), "");
        String ip = Fun.trimToDefault(httpRequest.getRemoteAddr(),"");

        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;

        JwtUser jwtUser = loginService.loginUser();
        AdminLog data = AdminLog.builder()
                .type("request")
                .ip(ip)
                .method(method)
                .url(url)
                .adminid(jwtUser.getId())
                .username(jwtUser.getUsername())
                .spendTime(spendTime)
                .saveTime(LocalDateTime.now())
                .status(1)
                .build();

        adminLogMapper.insertSelective(data);
    }
}
