package cn.cloud.core.base.advice;


import cn.cloud.core.rest.RestResponse;
import cn.cloud.core.rest.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获
 */
@ControllerAdvice(value = {"cn.cloud.controller"})
@Slf4j
public class ApiControllerAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RestResult errorHandler(Exception ex) {
        log.error(ex.getLocalizedMessage());

        return RestResponse.error(500, "Exception Error");
    }
}
