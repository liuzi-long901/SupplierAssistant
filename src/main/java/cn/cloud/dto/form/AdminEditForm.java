package cn.cloud.dto.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;


@Data
public class AdminEditForm {

    @ApiModelProperty(value = "11长度", required = true, dataType = "String")
    @NotNull
    @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "手机号不符合要求")
    private String username;

    @ApiModelProperty(value = "头像", required = false, dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "密码，字母大小写加数字，6-32长度", required = false, dataType = "String")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,32}$", message = "密码不符合要求")
    private String password;

    @ApiModelProperty(value = "状态", required = false, dataType = "Integer")
    @PositiveOrZero(message = "状态不合法")
    private Integer state;

    @ApiModelProperty(value = "角色(admin|user)", required = true, dataType = "String")
    @Pattern(regexp = "^(admin|user)$")
    private String role;

    private static AdminEditForm EMPTY = new AdminEditForm();

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
