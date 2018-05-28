package com.lch.cas.extras.service;

import com.lch.cas.extras.model.Group;
import com.lch.cas.extras.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupService userGroupService;

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Page<Group> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    public Page<Group> findAll(Specification<Group> spec, Pageable pageable) {
        return groupRepository.findAll(spec, pageable);
    }

    public Group findById(int id) {
        return groupRepository.findById(id);
    }

    public Group saveOrUpdate(Group group) {
        return groupRepository.save(group);
    }

    public void delete(Group group) {
        groupRepository.delete(group);
    }

    public Page<Group> findByAdminUserId(Specification<Group> spec, Pageable pageable, int userId) {
        List<Integer> groupIds = userGroupService.findByUserIdAndAdmin(userId, true).stream().map(userGroup -> userGroup.getGroupId()).collect(Collectors.toList());
        return groupRepository.findByIdIn(groupIds, pageable);
    }
}
