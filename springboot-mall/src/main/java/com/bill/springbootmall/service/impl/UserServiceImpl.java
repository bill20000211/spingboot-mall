package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dao.UserDao;
import com.bill.springbootmall.dto.UserLoginRequest;
import com.bill.springbootmall.dto.UserRegisterRequest;
import com.bill.springbootmall.model.User;
import com.bill.springbootmall.service.UserService;
import com.bill.springbootmall.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;  // 假設你有一個 JwtUtil 來生成 JWT token

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    // Service 叫做 register ，但 Dao 叫 createUser
    // 因為 Dao 的實作是真的新增帳號
    // 而 Service 並非只是新增帳號，還有做額外的判斷（如email）
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的 email
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            log.warn("此 Email {} 已被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼砸湊
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public String login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        // 檢查 user 是否存在
        if (user == null) {
            log.warn("此 Email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼砸湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        // 比較密碼
        // Java 比較字串一定要用 equals (記憶體位置)
        if (!user.getPassword().equals(hashedPassword)) {
            log.warn("Email {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 AuthenticationManager 進行身份驗證
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userLoginRequest.getEmail(), userLoginRequest.getPassword());
        authentication = authenticationManager.authenticate(authentication);

        // 認證成功後生成 JWT token
        String token = jwtUtil.createToken(authentication);

        System.out.println();

        // 返回 JWT token
        return token;
    }
}
