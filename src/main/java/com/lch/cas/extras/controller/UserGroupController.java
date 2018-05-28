package com.lch.cas.extras.controller;

import com.lch.cas.extras.common.CustomErrorType;
import com.lch.cas.extras.common.Utils;
import com.lch.cas.extras.model.UserGroup;
import com.lch.cas.extras.service.UserGroupService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user-group")
public class UserGroupController {

    public static final Logger logger = LoggerFactory.getLogger(UserGroupController.class);

    @Autowired
    UserGroupService userGroupService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "groupId,desc", paramType = "query")})
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<UserGroup>> list(HttpServletRequest request, HttpServletResponse response,
                                                @RequestHeader(value="X-USERID", required = false, defaultValue = "0") int userId, @RequestHeader(value="X-ROLE", required = false, defaultValue = "user") String role,
                                                @And ({@Spec(path = "groupId", spec = Equal.class), @Spec(path = "admin", spec = Equal.class)}) Specification<UserGroup> spec,
                                                @PageableDefault(size = 10, sort = "groupId") Pageable pageable) {
        if(role.equals("superAdmin")) {
            Page<UserGroup> page = userGroupService.findAll(spec, pageable);
            List<UserGroup> userGroups = page.getContent();
            Utils.setExtraHeader(response, page);
            return new ResponseEntity<>(userGroups, HttpStatus.OK);
        }
        else if(role.equals("admin")) {
            Page<UserGroup> page = userGroupService.findByUserIdAndAdmin(userId, true, pageable);
            List<UserGroup> userGroups = page.getContent();
            Utils.setExtraHeader(response, page);
            return new ResponseEntity<>(userGroups, HttpStatus.OK);
        }
        else {
            Utils.setExtraHeader(response, 0);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserGroup> findById(@PathVariable int id) {
        UserGroup userGroup = userGroupService.findById(id);
        if (userGroup == null) {
            logger.error("UserGroup with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserGroup>(userGroup, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<UserGroup> save(@RequestBody UserGroup userGroup) {
        if (StringUtils.isEmpty(userGroup.getGroupId()) || StringUtils.isEmpty(userGroup.getUserId())) {
            logger.error("UserGroup groupId or userId is empty.");
            return new ResponseEntity(new CustomErrorType(String.format("UserGroup groupId or userId is empty.")), HttpStatus.BAD_REQUEST);
        }
        UserGroup userGroupPersisted = userGroupService.saveOrUpdate(userGroup);
        if (userGroupPersisted == null) {
            logger.error("UserGroup with id {}/{} not found.", userGroup.getGroupId(), userGroup.getUserId());
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s/%s not found", userGroup.getGroupId(), userGroup.getUserId())), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserGroup>(userGroupPersisted, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserGroup> update(@PathVariable int id, @RequestBody UserGroup userGroup) {
        UserGroup userGroupPersisted = userGroupService.findById(id);
        if (userGroupPersisted == null) {
            logger.error("UserGroup with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        userGroupPersisted.setAdmin(userGroup.isAdmin());
        userGroupPersisted.setGroupId(userGroup.getGroupId());
        userGroupPersisted.setUserId(userGroup.getUserId());
        userGroupPersisted = userGroupService.saveOrUpdate(userGroupPersisted);
        return new ResponseEntity<UserGroup>(userGroupPersisted, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserGroup> delete(@PathVariable int id) {
        UserGroup userGroupPersisted = userGroupService.findById(id);
        if (userGroupPersisted == null) {
            logger.error("UserGroup with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("User with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        userGroupService.delete(userGroupPersisted);
        return new ResponseEntity<UserGroup>(userGroupPersisted, HttpStatus.OK);
    }


}
