package com.lch.cas.extras.service;

import com.lch.cas.extras.model.UserGroup;
import com.lch.cas.extras.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    public UserGroup findById(UserGroup.UserGroupCompositeId userGroupCompositeId) {
        return userGroupRepository.findOne(userGroupCompositeId);
    }

    public UserGroup saveOrUpdate(UserGroup userGroup) {
        return userGroupRepository.save(userGroup);
    }

    public void delete(UserGroup userGroup) {
        userGroupRepository.delete(userGroup);
    }
}