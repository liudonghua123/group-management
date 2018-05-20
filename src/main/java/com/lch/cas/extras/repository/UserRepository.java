package com.lch.cas.extras.repository;

import com.lch.cas.extras.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
	User save(User user);

	User findByEmail(String email);

	User findByUid(String uid);

	User findById(int id);
}
