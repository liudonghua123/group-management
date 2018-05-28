package com.lch.cas.extras.controller;

import com.lch.cas.extras.common.CustomErrorType;
import com.lch.cas.extras.common.Utils;
import com.lch.cas.extras.model.User;
import com.lch.cas.extras.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,desc", paramType = "query")})
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> list(HttpServletRequest request, HttpServletResponse response,
                                           @RequestHeader(value="X-USERID", required = false, defaultValue = "0") int userId, @RequestHeader(value="X-ROLE", required = false, defaultValue = "user") String role,
                                           @Spec(path = "uid", spec = Like.class) Specification<User> spec,
                                           @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        if(role.equals("superAdmin")) {
            Page<User> page = userService.findAll(spec, pageable);
            List<User> users = page.getContent();
            Utils.setExtraHeader(response, page);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        else if(role.equals("admin")) {
            Page<User> page = userService.findByAdminUserId(userId, pageable);
            List<User> users = page.getContent();
            Utils.setExtraHeader(response, page);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        else {
            User user = userService.findById(userId);
            List<User> users = Arrays.asList(user);
            Page<User> page = new PageImpl(users);
            Utils.setExtraHeader(response, 1);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findById(@PathVariable int id) {
        User user = userService.findById(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<User> save(@RequestBody User user) {
        if (StringUtils.isEmpty(user.getUid()) || StringUtils.isEmpty(user.getPassword())) {
            logger.error("User uid and password is empty.");
            return new ResponseEntity(new CustomErrorType(String.format("User uid and password is empty.")), HttpStatus.BAD_REQUEST);
        }
        // remove id
        user.setId(null);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        User userPersisted = userService.saveOrUpdate(user);
        if (userPersisted == null) {
            logger.error("User with id {} not found.", user.getId());
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s not found", user.getId())), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(userPersisted, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        User userPersisted = userService.findById(id);
        if (userPersisted == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        // update object properties
        updateUser(user, userPersisted);
        userPersisted = userService.saveOrUpdate(userPersisted);
        return new ResponseEntity<User>(userPersisted, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> delete(@PathVariable int id) {
        User userPersisted = userService.findById(id);
        if (userPersisted == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        userService.delete(userPersisted);
        return new ResponseEntity<User>(userPersisted, HttpStatus.OK);
    }

    private void updateUser(@RequestBody User user, User userPersisted) {
        if (!StringUtils.isEmpty(user.getUid())) {
            userPersisted.setUid(user.getUid());
        }
        if (!StringUtils.isEmpty(user.getPassword())) {
            userPersisted.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        if (!StringUtils.isEmpty(user.getUsername())) {
            userPersisted.setUsername(user.getUsername());
        }
        if (!StringUtils.isEmpty(user.getCategory())) {
            userPersisted.setCategory(user.getCategory());
        }
        if (user.getDepid() != 0) {
            userPersisted.setDepid(user.getDepid());
        }
        if (!StringUtils.isEmpty(user.getDepname())) {
            userPersisted.setDepname(user.getDepname());
        }
        if (!StringUtils.isEmpty(user.getIdnum())) {
            userPersisted.setIdnum(user.getIdnum());
        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            userPersisted.setEmail(user.getEmail());
        }
        if (!StringUtils.isEmpty(user.getPhone())) {
            userPersisted.setPhone(user.getPhone());
        }
        if (user.getDisable()) {
            userPersisted.setDisable(user.getDisable());
        }
        if (user.getExpired()) {
            userPersisted.setExpired(user.getExpired());
        }
    }


}
