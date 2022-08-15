package cn.cloud.controller.api;


import cn.cloud.core.base.BaseController;
import cn.cloud.core.rest.RestResponse;
import cn.cloud.core.rest.RestResult;
import cn.cloud.dto.login.LoginForm;
import cn.cloud.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api(tags = "登录")
@RestController
@Slf4j
@RequestMapping("/api/login")
public class LoginApiController extends BaseController {

    @Autowired
    private LoginService loginService;


    @ApiOperation("登陆接口")
    @PostMapping("")
    public RestResult<String> login(HttpServletRequest request, @Valid LoginForm loginForm, BindingResult result) {
        if (result.hasErrors()) {
            return RestResponse.fail(this.resultErrors(result));
        }

        String token = loginService.login(loginForm);

        if (token != null) {
            return RestResponse.data(token);
        }

        return RestResponse.fail("login failed");
    }
}
