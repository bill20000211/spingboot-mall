package com.bill.springbootmall.service;

import com.bill.springbootmall.dao.impl.UserDao;
import com.bill.springbootmall.dto.UserRegisterRequest;
import com.bill.springbootmall.model.User;
import com.bill.springbootmall.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
