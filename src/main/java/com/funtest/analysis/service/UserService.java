package com.funtest.analysis.service;

import java.math.BigInteger;

import com.funtest.analysis.bean.User;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

public interface UserService {
	/**
	 * 创建用户
	 * @param 
	 * @return	用户ID
	 */
	public Integer createUser(User User);
    
	/**
	 * 删除用户
	 * @param user
	 */
    public void deleteUser(User user);    
    
    /**
     * 更改用户表内容
     * @param user
     */
    public void updateUser(User user);
    	
    /**
     * 查询用户
     * @param id
     * @return
     */
    public User queryUser(Integer id);
    
    /**
     * 根据名字查询用户
     * @param name
     * @return
     */
    public User queryUserByName(String name);
    
    /**
     * 根据工号查询用户
     * @param name
     * @return
     */
    public User queryUserByEmployeeId(String employeeId);
    
    /**
     * 根据手机号查询用户
     * @param name
     * @return
     */
    public User queryUserByPhoneNo(String phoneNo);
    /**
     *查询用户数量
     * @return
     */
    public BigInteger queryUserRecords();
    
    /**
     * 分页查询
     * @param page
     * @param pageCondition
     * @return
     */
    public Page<User> queryPage(Page<User> page,PageCondition pageCondition);

    /**
     * 检测能否注册
     * @param user
     * @return	能注册：canRegister  不能注册：不能注册的信息
     */
	public String queryCanRegister(User user);
	
	/**
	 * 点赞
	 * @param id
	 * @return 更新后的点赞数
	 */
	public Integer updateAddStar(Integer id);



}
