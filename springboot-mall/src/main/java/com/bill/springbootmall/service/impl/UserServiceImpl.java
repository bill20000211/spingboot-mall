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
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
//        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        // 檢查 user 是否存在
//        if (user == null) {
//            log.warn("此 Email {} 尚未註冊", userLoginRequest.getEmail());
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }

        // 使用 MD5 生成密碼砸湊值
//        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        // 比較密碼
        // Java 比較字串一定要用 equals (記憶體位置)
//        if (!user.getPassword().equals(hashedPassword)) {
//            log.warn("Email {} 的密碼不正確", userLoginRequest.getEmail());
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }

        try {
            // 使用 AuthenticationManager 進行身份驗證
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userLoginRequest.getEmail(), userLoginRequest.getPassword());

            // 認證過程將使用 UserDetailsService 加載用戶信息，並使用配置的密碼編碼器進行密碼驗證
            authentication = authenticationManager.authenticate(authentication);

            // 認證成功後生成 JWT token
            String token = jwtUtil.createToken(authentication);

            // 返回 JWT token
            return token;

        } catch (BadCredentialsException e) {
            // 密碼不正確
            log.warn("密碼不正確: {}", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "密碼不正確");

        } catch (UsernameNotFoundException e) {
            // 用戶名不存在
            log.warn("此 Email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "此 Email 尚未註冊");

        } catch (LockedException e) {
            // 帳戶被鎖定
            log.warn("帳戶被鎖定: {}", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "帳戶已被鎖定");

        } catch (DisabledException e) {
            // 帳戶被禁用
            log.warn("帳戶已被禁用: {}", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "帳戶已被禁用");

        } catch (Exception e) {
            // 捕捉其他未預期的異常
            log.error("登入失敗: {}", userLoginRequest.getEmail(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "登入失敗，請稍後再試");
        }

    }
}
