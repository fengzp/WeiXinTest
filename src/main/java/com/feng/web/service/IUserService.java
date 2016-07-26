package com.feng.web.service;

import com.feng.web.model.User;

public interface IUserService {

	void addUser(User user);
	
	User getUserById(int id);
}
