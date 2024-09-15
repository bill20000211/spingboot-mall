package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dto.UserLoginRequest;
import com.bill.springbootmall.dto.UserRegisterRequest;
import com.bill.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest);

    User login(UserLoginRequest userLoginRequest);
}
