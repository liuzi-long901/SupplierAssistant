package cn.cloud.service;


import cn.cloud.dto.form.AdminAddForm;
import cn.cloud.dto.form.AdminEditForm;
import cn.cloud.dto.form.AdminSearchForm;
import cn.cloud.entity.Admin;
import cn.cloud.jwt.JwtUser;
import cn.cloud.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private LoginService loginService;

    public Admin get(Long id) {
        return exist(id);
    }

    public Admin exist(Long id) {
        return Optional.ofNullable(adminMapper.selectByPrimaryKey(id))
                .orElseThrow(() -> new EntityNotFoundException("ID 不存在"));
    }

    public Admin edit(Long id, AdminEditForm editForm) {
        exist(id);

        Admin editAdmin = Admin.builder().id(id).build();

        // 更新密码
        if (editForm.getPassword() != null) {
            editAdmin.setPassword(bCryptPasswordEncoder.encode(editForm.getPassword()));
        }

        // 其他字段更新
        Optional.ofNullable(editForm.getUsername()).ifPresent(editAdmin::setUsername);
        Optional.ofNullable(editForm.getAvatar()).ifPresent(editAdmin::setAvatar);
        Optional.ofNullable(editForm.getRole()).ifPresent(editAdmin::setRole);
        Optional.ofNullable(editForm.getState()).ifPresent(editAdmin::setState);

        JwtUser jwtUser = loginService.loginUser();
        editAdmin.setUpdatedBy(jwtUser.getId());

        adminMapper.updateByPrimaryKeySelective(editAdmin);

        return adminMapper.selectByPrimaryKey(editAdmin.getId());
    }

    public boolean remove(Long id) {
        exist(id);

        int rows = adminMapper.deleteByPrimaryKey(id);

        if (rows > 0) {
            return true;
        }

        return false;
    }

    public Admin save(AdminAddForm addForm) {
        Admin adminQuery = Admin.builder().username(addForm.getUsername()).build();

        Optional.ofNullable(adminMapper.selectOne(adminQuery))
                .ifPresent(o -> {
                    throw new EntityExistsException("用户名已存在");
                });

        JwtUser jwtUser = loginService.loginUser();

        Admin admin = Admin.builder()
                .username(addForm.getUsername())
                .avatar(addForm.getAvatar())
                .role(addForm.getRole())
                .createdBy(jwtUser.getId())
                .updatedBy(jwtUser.getId())
                .build();

        // 生成密码
        admin.setPassword(bCryptPasswordEncoder.encode(addForm.getPassword()));

        // 插入
        adminMapper.insertSelective(admin);

        return adminMapper.selectByPrimaryKey(admin.getId());
    }

    public List<Admin> search(AdminSearchForm searchForm) {
        return adminMapper.search(searchForm);
    }

    public Admin getByUsername(String username) {
        Admin adminQuery = Admin.builder().username(username).build();

        Admin admin = adminMapper.selectOne(adminQuery);

        if (admin == null) {
            throw new EntityNotFoundException("用户名不存在");
        }

        return admin;
    }

    public Admin getInfo(){
        JwtUser jwtUser = loginService.loginUser();
        return exist(jwtUser.getId());
    }
}
