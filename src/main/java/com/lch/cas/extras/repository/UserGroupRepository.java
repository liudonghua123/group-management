package com.lch.cas.extras.repository;

import com.lch.cas.extras.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroup.UserGroupCompositeId>, JpaSpecificationExecutor<UserGroup> {
}
