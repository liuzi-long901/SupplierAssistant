package cn.cloud.service;


import cn.cloud.dto.form.AdminLogSearchForm;
import cn.cloud.entity.AdminLog;
import cn.cloud.mapper.AdminLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminLogService {

    @Autowired
    private AdminLogMapper adminLogMapper;

    public List<AdminLog> search(AdminLogSearchForm searchForm) {
        return adminLogMapper.search(searchForm);
    }
}
