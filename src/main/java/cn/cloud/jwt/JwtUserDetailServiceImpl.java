package cn.cloud.jwt;


import cn.cloud.entity.Admin;
import cn.cloud.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin adminQuery = Admin.builder().username(username).build();
        Admin admin = adminMapper.selectOne(adminQuery);

        Optional.ofNullable(admin).orElseThrow(() -> new UsernameNotFoundException("admin not exist"));

        return JwtUser.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .state(admin.getState())
                .authorities(Collections.emptyList())
                .build();
    }
}
