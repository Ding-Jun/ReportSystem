package com.funtest.analysis.dao.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.funtest.analysis.bean.Role;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.RoleDao;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

@Repository
public class RoleDaoImpl extends BaseDao<Role> implements RoleDao {

	private static final String QUERY_ROLE_RECORDS="SELECT count(*) FROM `t_role` WHERE 1=1 ";
	private static final String QUERY_ROLE_PAGE="SELECT * FROM `t_role` WHERE 1=1 ";
	private static final String QUERY_ROLE_BY_NAME="SELECT * FROM `t_role` WHERE 1=1 AND `name`=? ";
	
	@Override
	public Integer createRole(Role Role) {
		return super.create(Role);
	}

	@Override
	public void deleteRole(Role Role) {
		super.delete(Role);
	}

	@Override
	public void updateRole(Role Role) {
		super.update(Role);
	}

	@Override
	public Role queryRole(Integer id) {
		return super.query(id);
	}

	@Override
	public Role queryRoleByName(String name) {
		String sql = QUERY_ROLE_BY_NAME.replaceFirst("[?]", "'"+name+"'");
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return (Role) query.addEntity(Role.class).uniqueResult();
	}

	@Override
	public BigInteger queryRoleRecords() {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_ROLE_RECORDS);
		return (BigInteger) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Role> queryPage(Page<Role> page, PageCondition pageCondition) {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_ROLE_PAGE);
		//页码从1开始
		query.setFirstResult((page.getCurPage()-1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		page.setRowData(query.addEntity(Role.class).list());
		return page;
	}
	
}
