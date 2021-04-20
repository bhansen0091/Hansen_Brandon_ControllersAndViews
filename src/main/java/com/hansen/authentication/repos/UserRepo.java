package com.hansen.authentication.repos;

import org.springframework.data.repository.CrudRepository;

import com.hansen.authentication.models.User;

public interface UserRepo extends CrudRepository<User, Long> {
	User findByEmail(String email);
}
