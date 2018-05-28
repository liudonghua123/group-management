package com.lch.cas.extras.service;

import com.lch.cas.extras.model.UserGroup;
import com.lch.cas.extras.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupService {
    @Autowired
    UserGroupRepository userGroupRepository;

    public List<UserGroup> findAll() {
        return userGroupRepository.findAll();
    }

    public Page<UserGroup> findAll(Pageable pageable) {
        return userGroupRepository.findAll(pageable);
    }

    public Page<UserGroup> findAll(Specification<UserGroup> spec, Pageable pageable) {
        return userGroupRepository.findAll(spec, pageable);
    }

    public UserGroup findById(Integer id) {
        return userGroupRepository.findOne(id);
    }

    public UserGroup saveOrUpdate(UserGroup userGroup) {
        return userGroupRepository.save(userGroup);
    }

    public void delete(UserGroup userGroup) {
        userGroupRepository.delete(userGroup);
    }

    public boolean isAdmin(int userId) {
        PageRequest pageable = new PageRequest(0, Integer.MAX_VALUE);
        Page<UserGroup> userGroups = userGroupRepository.findByUserIdAndAdmin(userId, true, pageable);
        return userGroups != null && userGroups.getContent().size() > 0;
    }

    public Page<UserGroup> findByUserIdAndAdmin(int userId, boolean admin, Pageable pageable) {
        return userGroupRepository.findByUserIdAndAdmin(userId, admin, pageable);
    }

    public List<UserGroup> findByUserIdAndAdmin(int userId, boolean admin) {
        return userGroupRepository.findByUserIdAndAdmin(userId, admin);
    }

    public List<UserGroup> findByGroupIdIn(List<Integer> groupIds) {
        return userGroupRepository.findByGroupIdIn(groupIds);
    }
}
