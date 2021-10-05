package com.codingdojo.exam.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.codingdojo.exam.models.User;
import com.codingdojo.exam.repository.UserRepository;

@Service
public class UserService {
	
private final UserRepository urepo;
	
	public UserService(UserRepository urepo) {
		this.urepo = urepo;
	}
	
	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return urepo.save(user);
	}
	
	public boolean authenticateUser(String email, String password) {
		User user = urepo.findByEmail(email);
		if(user == null) {
			return false;
		} else if(BCrypt.checkpw(password, user.getPassword())) {
			return true;
		}
		return false;
	}

}
