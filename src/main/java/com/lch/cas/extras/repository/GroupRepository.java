package com.lch.cas.extras.repository;

import com.lch.cas.extras.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>, JpaSpecificationExecutor<Group> {
    Group save(Group group);

    Group findById(int id);

    Page<Group> findByIdIn(List<Integer> groupIds, Pageable pageable);
}
