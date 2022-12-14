package cn.cloud.jwt;


import cn.cloud.core.rest.RestResponse;
import cn.cloud.core.rest.RestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("jwt authorized error: {}", e.getMessage());

        ServletOutputStream os = response.getOutputStream();

        RestResult<Object> error = RestResponse.error(HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage());
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(error);
        os.write(s.getBytes());
        os.flush();
        os.close();
    }
}
