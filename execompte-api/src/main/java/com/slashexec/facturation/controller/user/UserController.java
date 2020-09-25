package com.slashexec.facturation.controller.user;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.slashexec.facturation.model.ActivityReport;
import com.slashexec.facturation.model.Company;
import com.slashexec.facturation.model.User;
import com.slashexec.facturation.model.SignUpInfo;

@RestController
@CrossOrigin(origins = "https://4200-c4fb21c2-9271-4845-8037-38cf0ae44765.ws-eu01.gitpod.io")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/users")
	public ResponseEntity<User> signup(@RequestBody SignUpInfo signUpInfo) {
		
		User createdUser = userService.signup(signUpInfo);
		//We want to return the URI of the new resource
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.getId()).toUri();
		
		//LOCATION is not exposed by default (CORS)
		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION);
		
		return new ResponseEntity<User> (createdUser, respHeaders, HttpStatus.CREATED);
    }
    
    @GetMapping("/users")
	public List<User> getUsers() {
		return userService.findAll();
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable String id) {
		
		User user = userService.findById(id);
		if (user != null) {
			return new ResponseEntity<User> (user, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/users/{userName}/")
	public ResponseEntity<User> updateUserCompany(@PathVariable String userName, @RequestBody Company company) {
		User userFound = userService.findByUserName(userName);
		if (userFound == null) {
			return ResponseEntity.notFound().build();
		}
		
		User updatedUser = userService.updateUserCompany(userFound, company);
		return new ResponseEntity<User> (updatedUser, HttpStatus.OK);
	}

}
