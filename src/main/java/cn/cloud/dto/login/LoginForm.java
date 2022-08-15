package cn.cloud.dto.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginForm {
    @ApiModelProperty(value = "用户名", required = true, dataType = "String")
    @NotNull(message = "请输入用户名")
    private String username;

    @ApiModelProperty(value = "密码", required = true, dataType = "String")
    @NotNull(message = "请输入密码")
    private String password;
}
