package cn.cloud.service;


import cn.cloud.dto.login.LoginForm;
import cn.cloud.entity.Admin;
import cn.cloud.entity.AdminLog;
import cn.cloud.jwt.JwtTokenProvider;
import cn.cloud.jwt.JwtUser;
import cn.cloud.mapper.AdminLogMapper;
import cn.cloud.mapper.AdminMapper;
import io.github.xfuns.java.Fun;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminLogMapper adminLogMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    public String login(LoginForm loginForm) {
        long startTime = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();
        String requestUri = Fun.trimToDefault(httpRequest.getRequestURI(), "");
        String queryString = Fun.trimToDefault(httpRequest.getQueryString(), "");
        String url = Fun.empty(queryString) ? requestUri : requestUri + "?" + queryString;
        String ip = Fun.trimToDefault(httpRequest.getRemoteAddr(),"");
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;

        AdminLog adminLog = AdminLog.builder()
                .type("login")
                .ip(ip)
                .method("POST")
                .url(url)
                .saveTime(now)
                .build();

        // 登陆
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
            );
            JwtUser jwtUser = (JwtUser) authenticate.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtTokenProvider.generateTokenByString(authenticate);

            // 登录日志
            adminLog.setAdminid(jwtUser.getId());
            adminLog.setUsername(jwtUser.getUsername());
            adminLog.setSpendTime(System.currentTimeMillis() - startTime);
            adminLog.setStatus(1);
            adminLogMapper.insertSelective(adminLog);

            return token;
        } catch (Exception e) {
            // 登陆失败日志（如密码错误）
            Admin admin = adminMapper.selectOne(Admin.builder().username(loginForm.getUsername()).build());
            if (admin != null) {
                adminLog.setAdminid(admin.getId());
                adminLog.setUsername(admin.getUsername());
                adminLog.setSpendTime(System.currentTimeMillis() - startTime);
                adminLog.setStatus(0);
                adminLogMapper.insertSelective(adminLog);
            }
            log.warn(e.getLocalizedMessage());
            return null;
        }
    }

    public JwtUser loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUser) authentication.getPrincipal();
    }
}
