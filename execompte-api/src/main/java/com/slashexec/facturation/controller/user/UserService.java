package com.slashexec.facturation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slashexec.facturation.model.User;
import com.slashexec.facturation.model.Company;
import com.slashexec.facturation.model.SignUpInfo;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	public User signup(SignUpInfo signUpInfo) {
		User user = new User();
		user.setUserName(signUpInfo.getUserName());
		user.setPassword(signUpInfo.getPassword());
		return userRepository.save(user);
	}

	public User findById(String id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName).orElse(null);
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public User updateUserCompany(User userFound, Company company) {
		userFound.setCompany(company);
		return  userRepository.save(userFound);
	}

}
