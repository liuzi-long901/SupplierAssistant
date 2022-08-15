package cn.cloud.controller.api;



import cn.cloud.core.base.BaseController;
import cn.cloud.core.rest.RestResponse;
import cn.cloud.core.rest.RestResult;
import cn.cloud.dto.form.AdminLogSearchForm;
import cn.cloud.entity.AdminLog;
import cn.cloud.service.AdminLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "日志")
@RestController
@Slf4j
@RequestMapping("/api/admin_log")
public class AdminLogApiController extends BaseController {
    @Resource
    private AdminLogService adminLogService;

    @ApiOperation("列表查询")
    @GetMapping("/search")
    public RestResult<PageInfo> search(@Valid AdminLogSearchForm searchForm, BindingResult result) {
        if (result.hasErrors()) {
            return RestResponse.fail(this.resultErrors(result));
        }

        Integer page = searchForm.getPage();
        Integer size = searchForm.getSize();
        page = page == null ? 1 : Math.max(1, page);
        size = size == null ? pageSize : Math.max(1, size);

        PageHelper.startPage(page, size);
        List<AdminLog> datas = adminLogService.search(searchForm);
        PageInfo pageAdmins = new PageInfo(datas);

        return RestResponse.data(pageAdmins);
    }
}
