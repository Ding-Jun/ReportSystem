package com.funtest.analysis.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.funtest.analysis.bean.Admin;
import com.funtest.analysis.bean.Permission;
import com.funtest.analysis.dao.AdminDao;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

@Repository
public class AdminDaoImpl extends BaseDao<Admin> implements AdminDao{

	private static final String QUERY_ADMIN_RECORDS="SELECT count(*) FROM `t_admin` WHERE 1=1 AND `name`!='root'";
	private static final String QUERY_ADMIN_PAGE="SELECT * FROM `t_admin` WHERE  1=1 AND `name`!='root' ";
	private static final String QUERY_ADMIN_BY_NAME="SELECT * FROM `t_admin` WHERE 1=1  AND `name`=? ";
	private static final String QUERY_ADMIN_PERMISSIONS_FOR_SHIRO="SELECT permissions FROM v_admin_permissions WHERE 1=1 AND `adminId`=? ";

	@Override
	public Integer createAdmin(Admin admin) {
		return super.create(admin);
	}

	@Override
	public void deleteAdmin(Admin admin) {
		super.delete(admin);
	}

	@Override
	public void updateAdmin(Admin admin) {
		
		super.currentSession().merge(admin);
	}

	@Override
	public Admin queryAdmin(Integer id) {
		return super.query(id);
	}

	@Override
	public Admin queryAdminByName(String name) {
		if(name==null || name.trim() == "") return null;
		
		String sql = QUERY_ADMIN_BY_NAME.replaceFirst("[?]", "'"+name+"'");
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return (Admin) query.addEntity(Admin.class).uniqueResult();
	}

	@Override
	public BigInteger queryAdminRecords() {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_ADMIN_RECORDS);
		return (BigInteger) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Admin> queryPage(Page<Admin> page, PageCondition pageCondition) {
		String condition="";
		if(pageCondition!=null){
			condition=" "+pageCondition.getOrder()+" "+pageCondition.getSort();
		}
	
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_ADMIN_RECORDS+condition);
		BigInteger records= (BigInteger) query.uniqueResult();
		query = this.currentSession().createSQLQuery(QUERY_ADMIN_PAGE+condition);
		
		//页码从1开始
		query.setFirstResult((page.getCurPage()-1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		page.setRowData(query.addEntity(Admin.class).list());
		page.setTotalRows(records.longValue());
		page.setTotalPage((long) Math.ceil(page.getTotalRows()/(double)page.getPageSize()) );
		return page;
	}

	@Override
	public List<Permission> queryPermissions(Integer adminId) {
		return this.queryAdmin(adminId).getPermissions();
	}

	@SuppressWarnings("unchecked")
	public List<String> queryAdminPermissionsForShiro(Integer adminId) {
		if(adminId == null){
			return null;
		}
		String sql = QUERY_ADMIN_PERMISSIONS_FOR_SHIRO.replaceFirst("[?]", adminId.toString());
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return query.list();
	}	

	
}
