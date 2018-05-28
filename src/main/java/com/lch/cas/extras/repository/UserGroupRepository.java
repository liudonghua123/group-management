package com.lch.cas.extras.repository;

import com.lch.cas.extras.model.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer>, JpaSpecificationExecutor<UserGroup> {
    Page<UserGroup> findByUserIdAndAdmin(int userId, boolean admin, Pageable pageable);

    List<UserGroup> findByUserIdAndAdmin(int userId, boolean admin);

    List<UserGroup> findByGroupIdIn(List<Integer> groupIds);
}
