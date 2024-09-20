package com.bill.springbootmall.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return user.getUserId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 根據你的 User 類添加角色邏輯。這裡假設每個用戶只有 "ROLE_USER"
        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(user.getAuthority().split(",")).map(
                        SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // 使用 email 作為 username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 根據需求進行修改
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 根據需求進行修改
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 根據需求進行修改
    }

    @Override
    public boolean isEnabled() {
        return true;  // 根據需求進行修改
    }
}
