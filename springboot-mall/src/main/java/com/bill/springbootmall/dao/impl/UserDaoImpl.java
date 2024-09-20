package com.bill.springbootmall.dao.impl;

import com.bill.springbootmall.dao.UserDao;
import com.bill.springbootmall.dto.UserRegisterRequest;
import com.bill.springbootmall.model.User;
import com.bill.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email,password, authority, created_date, last_modified_date " +
                "FROM `user` WHERE user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (userList.size() > 0) {
            return userList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, authority, created_date, last_modified_date " +
                "FROM `user` WHERE email = :email";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (userList.size() > 0) {
            return userList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO `user`(email, password,authority ,created_date, last_modified_date)" +
                "VALUES (:email, :password,:authority ,:createdDate, :lastModifiedDate)";
        // Map<User物件裡的參數名稱, 物件>
        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());
        map.put("authority", userRegisterRequest.getAuthority());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer userId = keyHolder.getKey().intValue();

        return userId;
    }
}
