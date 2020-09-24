package com.slashexec.facturation.controller.user;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.slashexec.facturation.model.User;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {

	Optional<User> findByUserName(String username);

}
