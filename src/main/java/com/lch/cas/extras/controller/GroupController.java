package com.lch.cas.extras.controller;

import com.lch.cas.extras.common.CustomErrorType;
import com.lch.cas.extras.common.Utils;
import com.lch.cas.extras.model.Group;
import com.lch.cas.extras.service.GroupService;
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
@RequestMapping("/api/group")
public class GroupController {

    public static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    GroupService groupService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,desc", paramType = "query")})
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Group>> list(HttpServletRequest request, HttpServletResponse response,
                                            @RequestHeader(value="X-USERID", required = false, defaultValue = "0") int userId, @RequestHeader(value="X-ROLE", required = false, defaultValue = "user") String role,
                                            @Spec(path = "name", spec = Like.class) Specification<Group> spec,
                                            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        if(role.equals("superAdmin")) {
            Page<Group> page = groupService.findAll(spec, pageable);
            List<Group> groups = page.getContent();
            Utils.setExtraHeader(response, page);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        }
        else if(role.equals("admin")) {
            Page<Group> page = groupService.findByAdminUserId(spec, pageable, userId);
            List<Group> groups = page.getContent();
            Utils.setExtraHeader(response, page);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        }
        else {
            Utils.setExtraHeader(response, 0);
            return new ResponseEntity<>(new ArrayList<Group>(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Group> findById(@PathVariable int id) {
        Group group = groupService.findById(id);
        if (group == null) {
            logger.error("Group with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("Group with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Group>(group, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Group> save(@RequestBody Group group) {
        if (StringUtils.isEmpty(group.getName())) {
            logger.error("Group name is empty.");
            return new ResponseEntity(new CustomErrorType(String.format("Group name is empty.")), HttpStatus.BAD_REQUEST);
        }
        // remove id
        group.setId(null);
        Group groupPersisted = groupService.saveOrUpdate(group);
        if (groupPersisted == null) {
            logger.error("Group with id {} not found.", group.getId());
            return new ResponseEntity(new CustomErrorType(String.format("Group with id %s not found", group.getId())), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Group>(groupPersisted, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Group> update(@PathVariable int id, @RequestBody Group group) {
        Group groupPersisted = groupService.findById(id);
        if (groupPersisted == null) {
            logger.error("Group with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("Group with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        // update object properties
        updateGroup(group, groupPersisted);
        groupPersisted = groupService.saveOrUpdate(groupPersisted);
        return new ResponseEntity<Group>(groupPersisted, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Group> delete(@PathVariable int id) {
        Group groupPersisted = groupService.findById(id);
        if (groupPersisted == null) {
            logger.error("Group with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType(String.format("Group with id %s not found", id)), HttpStatus.NOT_FOUND);
        }
        groupService.delete(groupPersisted);
        return new ResponseEntity<Group>(groupPersisted, HttpStatus.OK);
    }

    private void updateGroup(@RequestBody Group group, Group groupPersisted) {
        if (!StringUtils.isEmpty(group.getName())) {
            groupPersisted.setName(group.getName());
        }
    }


}
