package com.funtest.analysis.service.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.funtest.analysis.bean.Comment;
import com.funtest.analysis.bean.User;
import com.funtest.analysis.dao.UserDao;
import com.funtest.analysis.service.UserService;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao dao;
	
	@Override
	
	public Integer createUser(User user) {
		if(dao.queryUserByEmployeeId(user.getEmployeeId()) != null){
			throw new RuntimeException("提示：该工号已注册！");
		}
		if(dao.queryUserByName(user.getName()) != null){
			throw new RuntimeException("提示：该姓名已注册！");
		}
		if(dao.queryUserByPhoneNo(user.getPhoneNo()) != null){
			throw new RuntimeException("提示：该手机号已注册！");
		}

		user.setPassword("123456");
		user.setUserStar(new UserStar());
		return dao.createUser(user);
	}

	@Override
	
	public void deleteUser(User user) {
		User u = dao.queryUser(user.getId());
		if(u !=null){

			dao.deleteUser(u);
		}
	}

	@Override
	
	public void updateUser(User user) {
		User u =dao.queryUser(user.getId());
		u.setName(user.getName());
		u.setEmployeeId(user.getEmployeeId());
		u.setPhoneNo(user.getPhoneNo());
		u.setAvatar(user.getAvatar());
		u.setUserRegion(user.getUserRegion());
		u.setUserServiceItem(user.getUserServiceItem());
		dao.updateUser(u);
	}

	@Override
	public User queryUser(Integer id) {
		return dao.queryUser(id);
	}

	@Override
	public User queryUserByName(String name) {
		return dao.queryUserByName(name);
	}

	@Override
	public BigInteger queryUserRecords() {
		return dao.queryUserRecords();
	}

	@Override
	public Page<User> queryPage(Page<User> page, PageCondition pageCondition) {
		return dao.queryPage(page, pageCondition);
	}

	@Override
	public User queryUserByEmployeeId(String employeeId) {
		return dao.queryUserByEmployeeId(employeeId);
	}

	@Override
	public User queryUserByPhoneNo(String phoneNo) {
		return dao.queryUserByPhoneNo(phoneNo);
	}

	@Override
	public String queryCanRegister(User user) {
		if(dao.queryUserByEmployeeId(user.getEmployeeId()) != null){
			return "该工号已注册！";
		}
		if(dao.queryUserByName(user.getName()) != null){
			return "该姓名已注册！";
		}
		if(dao.queryUserByPhoneNo(user.getPhoneNo()) != null){
			return "该手机号已注册！";
		}
		return "canRegister";
	}

	@Override
	public Integer updateAddStar(Integer id) {
		User user = dao.queryUser(id);
		Integer starCount = user.getUserStar().getStarCount();
		starCount+=1;
		user.getUserStar().setStarCount(starCount);
		dao.updateUser(user);
		return starCount;
	}

	@Override
	public Integer createComment(Integer userId, Comment comment) {
		return null;
	}

}
