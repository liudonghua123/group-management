package com.lch.cas.extras.controller;

import com.lch.cas.extras.config.AppConfig;
import com.lch.cas.extras.model.User;
import com.lch.cas.extras.service.UserGroupService;
import com.lch.cas.extras.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
@Configuration
public class AuthController {

    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<TokenEntity> login(@RequestParam String uid, @RequestParam String password) throws ServletException {
        User user = userService.findByUid(uid);
        if (user == null) {
            throw new ServletException(String.format("User logined with %s/%s not found.", uid, password));
        }
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equalsIgnoreCase(user.getPassword())) {
            throw new ServletException("Invalid login. Please check your uid and password.");
        }
        // 获取用户角色，lch为superAdmin, userGroup中admin为true的用户是admin，其他用户是user
        // superAdmin 具有所有权限，admin可以修改自己账户以及管理userGroup，user可以修改自己账户
        String role = "user";
        if(user.getUid().equals("lch")) {
            role = "superAdmin";
        }
        else if(userGroupService.isAdmin(user.getId())) {
            role = "admin";
        }
        String jwtToken = Jwts.builder().setSubject(uid).claim("roles", role).setIssuedAt(new Date()).claim("userId", user.getId())
                .signWith(SignatureAlgorithm.HS256, appConfig.getSecretkey()).compact();
        return new ResponseEntity<TokenEntity>(new TokenEntity(jwtToken), HttpStatus.OK);
    }


    static class TokenEntity {
        String token;

        TokenEntity(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
