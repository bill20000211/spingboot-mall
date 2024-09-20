package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dao.UserDao;
import com.bill.springbootmall.model.MyUserDetails;
import com.bill.springbootmall.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public MyUserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // 將 User 封裝為 MyUserDetails 並返回
        return new MyUserDetails(user);
    }
}
