package com.funtest.analysis.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.funtest.analysis.bean.Resource;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.ResourceDao;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

@Repository
public class ResourceDaoImpl extends BaseDao<Resource> implements ResourceDao {

	private static final String QUERY_RESOURCE_RECORDS="SELECT count(*) FROM `t_resource` WHERE 1=1 ";
	private static final String QUERY_RESOURCE_PAGE="SELECT * FROM `t_resource` WHERE 1=1 ";
	private static final String QUERY_RESOURCE_BY_NAME="SELECT * FROM `t_resource` WHERE 1=1 AND `name`=? ";
	private static final String QUERY_RESOURCE="SELECT t_resource.`name` AS resource FROM t_permission AS p "
				+"INNER JOIN t_admin_permission AS ap ON ap.permissionId = p.id "
				+"INNER JOIN t_resource ON p.ResourceId = t_resource.id "
				+"WHERE ap.adminId = ? GROUP BY p.ResourceId";
	@Override
	public Integer createResource(Resource Resource) {
		return super.create(Resource);
	}

	@Override
	public void deleteResource(Resource Resource) {
		super.delete(Resource);
	}

	@Override
	public void updateResource(Resource Resource) {
		super.update(Resource);
	}

	@Override
	public Resource queryResource(Integer id) {
		return super.query(id);
	}

	@Override
	public Resource queryResourceByName(String name) {
		String sql = QUERY_RESOURCE_BY_NAME.replaceFirst("[?]", "'"+name+"'");
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return (Resource) query.addEntity(Resource.class).uniqueResult();
	}

	@Override
	public BigInteger queryResourceRecords() {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_RESOURCE_RECORDS);
		return (BigInteger) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Resource> queryPage(Page<Resource> page, PageCondition pageCondition) {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_RESOURCE_PAGE);
		//页码从1开始
		query.setFirstResult((page.getCurPage()-1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		page.setRowData(query.addEntity(Resource.class).list());
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> queryAllResources() {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_RESOURCE_PAGE);
		return query.addEntity(Resource.class).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> queryResources(Integer adminId){
		String sql=QUERY_RESOURCE.replaceFirst("[?]", adminId.toString());
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return query.list();
	}
}
