package com.lch.cas.extras.service;

import com.lch.cas.extras.model.User;
import com.lch.cas.extras.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;



	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public Page<User> findAll(Specification<User> spec, Pageable pageable) {
		return userRepository.findAll(spec, pageable);
	}

	public User findById(int id) {
		return userRepository.findById(id);
	}

	public User findByUid(String uid) {
		return userRepository.findByUid(uid);
	}

	public User saveOrUpdate(User user) {
		return userRepository.save(user);
	}

	public void delete(User user) {
		userRepository.delete(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}


}
