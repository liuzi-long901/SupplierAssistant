package cn.cloud.dto.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class AdminLogSearchForm {
    @ApiModelProperty(value = "状态", required = false, dataType = "Integer")
    @PositiveOrZero
    private Integer status;

    @ApiModelProperty(value = "用户名前缀模糊查询", required = false, dataType = "String")
    @Size(min = 1, max = 32)
    private String username;

    @ApiModelProperty(value = "类型", required = false, dataType = "String")
    private String type;

    @ApiModelProperty(value = "IP地址", required = false, dataType = "String")
    private String ip;

    @ApiModelProperty(value = "排序字段(id|spendTime|saveTime)，默认为id", required = false, dataType = "String")
    @Pattern(regexp = "(id|spendTime|saveTime)", message = "排序字段不合法")
    private String orderby;

    @ApiModelProperty(value = "排序(desc|asc)，默认为desc", required = false, dataType = "String")
    @Pattern(regexp = "(desc|asc)", message = "排序不合法")
    private String order;

    @ApiModelProperty(value = "分页页码，默认第1页", required = false, dataType = "Integer")
    private Integer page;

    @ApiModelProperty(value = "分页数量，默认20条", required = false, dataType = "Integer")
    private Integer size;
}
