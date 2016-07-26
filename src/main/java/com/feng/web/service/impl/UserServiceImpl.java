package com.feng.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feng.web.dao.UserMapper;
import com.feng.web.model.User;
import com.feng.web.service.IUserService;

/**
 * 
 * @author fengzp
 *
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;
	
	public void addUser(User user) {
		userMapper.insert(user);
	}

	public User getUserById(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

}
