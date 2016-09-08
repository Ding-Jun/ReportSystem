package com.funtest.analysis.dao.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.funtest.analysis.bean.User;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.UserDao;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

@Repository
public class UserDaoImpl extends BaseDao<User> implements UserDao{

	private static final String QUERY_USER_RECORDS="SELECT count(*) FROM `t_user` WHERE 1=1 ";
	private static final String QUERY_USER_PAGE="SELECT * FROM `t_user` WHERE 1=1 ";
	private static final String QUERY_USER_BY_NAME="SELECT * FROM `t_user` WHERE 1=1 AND `name`=? ";
	private static final String QUERY_USER_BY_EMPLOYEE_ID="SELECT * FROM `t_user` WHERE 1=1 AND `employeeId`=? ";
	private static final String QUERY_USER_BY_PHONE_NO="SELECT * FROM `t_user` WHERE 1=1 AND `phoneNo`=? ";
	
	@Override
	public Integer createUser(User User) {
		return super.create(User);
	}

	@Override
	public void deleteUser(User User) {
		super.delete(User);
	}

	@Override
	public void updateUser(User User) {
		
		super.update(User);
	}

	@Override
	public User queryUser(Integer id) {
		return super.query(id);
	}

	@Override
	public User queryUserByName(String name) {
		String sql = QUERY_USER_BY_NAME.replaceFirst("[?]", "'"+name+"'");
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return (User) query.addEntity(User.class).uniqueResult();
	}

	@Override
	public BigInteger queryUserRecords() {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_USER_RECORDS);
		return (BigInteger) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<User> queryPage(Page<User> page, PageCondition pageCondition) {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_USER_PAGE);
		//页码从1开始
		query.setFirstResult((page.getCurPage()-1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		page.setRowData(query.addEntity(User.class).list());
		return page;
	}

	@Override
	public User queryUserByEmployeeId(String employeeId) {
		String sql = QUERY_USER_BY_EMPLOYEE_ID.replaceFirst("[?]", "'"+employeeId+"'");
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return (User) query.addEntity(User.class).uniqueResult();
	}

	@Override
	public User queryUserByPhoneNo(String phoneNo) {
		String sql = QUERY_USER_BY_PHONE_NO.replaceFirst("[?]", "'"+phoneNo+"'");
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return (User) query.addEntity(User.class).uniqueResult();
	}
	
}
