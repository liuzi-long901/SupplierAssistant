package cn.cloud.mapper;


import cn.cloud.dto.form.AdminSearchForm;
import cn.cloud.entity.Admin;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface
AdminMapper extends Mapper<Admin> {
    List<Admin> search(AdminSearchForm searchForm);
}
