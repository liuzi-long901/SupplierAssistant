package cn.cloud.dto.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Login {
    @ApiModelProperty(value = "用户名", required = true, dataType = "String")
    @NotNull(message = "请输入用户名")
    private String username;


    @ApiModelProperty(value = "验证码",required = false,dataType = "String")
    private String code;
}
