package cn.cloud.controller.api;


import cn.cloud.config.zhenziCode;
import cn.cloud.core.base.BaseController;
import cn.cloud.core.rest.RestResponse;
import cn.cloud.core.rest.RestResult;
import cn.cloud.dto.login.Login;
import cn.cloud.dto.login.LoginForm;
import cn.cloud.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    private zhenziCode zhenziCode;

    /***
     * 验证手机号码 并且发送验证码
     * @param request
     * @param phoneNum
     * @return
     * @throws Exception
     */
    @PostMapping("/PhoneNumCode")
    public RestResult<String> toCode(HttpServletRequest request, String phoneNum) throws Exception {
        HttpSession session = request.getSession(true);
        String random = zhenziCode.printRandom();
        session.setAttribute("randStr",random);
        return RestResponse.success(zhenziCode.sendMessage(random,phoneNum));
    }


    @ApiOperation("验证码登陆接口")
    @PostMapping("/code")
    public RestResult<String> loginByCode(HttpServletRequest request, @Valid Login loginForm, BindingResult result) {
        if (result.hasErrors()) {
            return RestResponse.fail(this.resultErrors(result));
        }

        String token = loginService.loginByCode(loginForm);

        if (token != null) {
            return RestResponse.data(token);
        }

        return RestResponse.fail("login failed");
    }
}
