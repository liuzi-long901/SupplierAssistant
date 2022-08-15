package cn.cloud.mapper;


import cn.cloud.dto.form.AdminLogSearchForm;
import cn.cloud.entity.AdminLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AdminLogMapper extends Mapper<AdminLog> {
    List<AdminLog> search(AdminLogSearchForm searchForm);
}
