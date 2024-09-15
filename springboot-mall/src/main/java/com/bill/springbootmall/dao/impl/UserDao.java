package com.bill.springbootmall.dao.impl;

import com.bill.springbootmall.dto.UserRegisterRequest;
import com.bill.springbootmall.model.User;

public interface UserDao {

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    Integer createUser(UserRegisterRequest userRegisterRequest);
}
