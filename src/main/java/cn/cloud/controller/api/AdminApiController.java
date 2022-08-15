package cn.cloud.controller.api;


import cn.cloud.core.base.BaseController;
import cn.cloud.core.rest.RestError;
import cn.cloud.core.rest.RestResponse;
import cn.cloud.core.rest.RestResult;
import cn.cloud.dto.form.AdminAddForm;
import cn.cloud.dto.form.AdminEditForm;
import cn.cloud.dto.form.AdminSearchForm;
import cn.cloud.entity.Admin;
import cn.cloud.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.xfuns.java.Fun;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "管理员")
@RestController
@Slf4j
@RequestMapping("/api/admin")
public class AdminApiController extends BaseController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("详情，ID查找")
    @GetMapping("/{id}")
    public RestResult<Admin> get(@PathVariable Long id) {
        if (id > 0) {
            try {
                Admin data = adminService.get(id);

                return RestResponse.data(data);
            } catch (Exception e) {
                return RestResponse.fail(e.getLocalizedMessage());
            }
        }

        return RestResponse.fail(RestError.PARAM_ERROR);
    }

    @ApiOperation("详情，username查找")
    @GetMapping("/username")
    public RestResult<Admin> getByUsername(@RequestParam("username") String username) {
        if (!Fun.blank(username)) {
            try {
                Admin data = adminService.getByUsername(username);

                return RestResponse.data(data);
            } catch (Exception e) {
                return RestResponse.fail(e.getLocalizedMessage());
            }
        }

        return RestResponse.fail(RestError.PARAM_ERROR);
    }

    @ApiOperation("新增")
    @PostMapping("")
    public RestResult<Admin> add(@Valid AdminAddForm addForm, BindingResult result) {
        if (result.hasErrors()) {
            return RestResponse.fail(this.resultErrors(result));
        }

        try {
            Admin data = adminService.save(addForm);

            return RestResponse.data(data);
        } catch (Exception e) {
            return RestResponse.fail(e.getLocalizedMessage());
        }
    }

    @ApiOperation("修改")
    @PostMapping("/edit/{id}")
    public RestResult<Admin> edit(@PathVariable Long id, @Valid AdminEditForm editForm, BindingResult result) {
        if (result.hasErrors()) {
            return RestResponse.fail(this.resultErrors(result));
        }

        if (id > 0 && !editForm.isEmpty()) {
            try {
                Admin data = adminService.edit(id, editForm);
                return RestResponse.data(data);
            } catch (Exception e) {
                return RestResponse.fail(e.getLocalizedMessage());
            }
        }

        return RestResponse.fail(RestError.PARAM_ERROR);
    }

    @ApiOperation("删除")
    @PostMapping("/remove/{id}")
    public RestResult remove(@PathVariable Long id) {
        if (id > 0) {
            boolean result = adminService.remove(id);
            if (result) {
                return RestResponse.success();
            }
        }

        return RestResponse.fail(RestError.PARAM_ERROR);
    }

    @ApiOperation("列表查询")
    @GetMapping("/search")
    public RestResult<PageInfo> search(@Valid AdminSearchForm searchForm, BindingResult result) {
        if (result.hasErrors()) {
            return RestResponse.fail(this.resultErrors(result));
        }

        Integer page = searchForm.getPage();
        Integer size = searchForm.getSize();
        page = page == null ? 1 : Math.max(1, page);
        size = size == null ? pageSize : Math.max(1, size);

        PageHelper.startPage(page, size);
        List<Admin> datas = adminService.search(searchForm);
        PageInfo pageAdmins = new PageInfo(datas);

        return RestResponse.data(pageAdmins);
    }

    @GetMapping("/getInfo")
    @ApiOperation("token获取用户信息")
    public RestResult<Admin> getInfo(HttpServletRequest request) {
        String token = request.getHeader("Jwt-Authorization");
        if(token!=null){
            Admin info = adminService.getInfo();
            return RestResponse.data(info);
        }
        return RestResponse.fail("token not null!");
    }
}
