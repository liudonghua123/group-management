package com.lch.cas.extras.repository;

import com.lch.cas.extras.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User save(User user);

    User findByEmail(String email);

    User findByUid(String uid);

    User findById(int id);

    Page<User> findByIdIn(List<Integer> userIds, Pageable pageable);
}
