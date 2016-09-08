package com.funtest.analysis.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.funtest.analysis.bean.Permission;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.PermissionDao;

@Repository
public class PermissionDaoImpl extends BaseDao<Permission> implements PermissionDao {
	
	private static final String QUERY_PERMISSION_BY_NAME="SELECT * FROM `t_permission` WHERE 1=1 AND `name`=? ";
	private static final String QUERY_PERMISSION_PAGE="SELECT * FROM `t_permission` WHERE 1=1 ";
	private static final String QUERY_ADMIN_PERMISSION_BY_RESOURCE="SELECT p.name AS permission "
																	+"FROM t_permission AS p "
																	+"INNER JOIN t_admin_permission AS ap ON ap.permissionId = p.id "
																	+"INNER JOIN t_resource AS r ON p.ResourceId = r.id "
																	+"WHERE ap.adminId = ? AND r.`name`='?'";

	@Override
	public Integer createPermission(Permission permission) {
		return super.create(permission);
	}

	@Override
	public void deletePermission(Permission permission) {
		super.delete(permission);
		
	}

	@Override
	public void updatePermission(Permission permission) {
		super.update(permission);
		
	}

	@Override
	public Permission queryPermission(Integer id) {
		return super.query(id);
	}

	@Override
	public Permission queryPermissionByName(String name) {
		String sql = QUERY_PERMISSION_BY_NAME.replaceFirst("[?]", "'"+name+"'");
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return (Permission) query.addEntity(Permission.class).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> queryAllPermissions() {
		SQLQuery query = this.currentSession().createSQLQuery(QUERY_PERMISSION_PAGE);
		return query.addEntity(Permission.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryAdminPermissionsByResource(Integer adminId ,String resourceName) {
		// TODO Auto-generated method stub
		String sql = QUERY_ADMIN_PERMISSION_BY_RESOURCE.replaceFirst("[?]", adminId.toString())
														.replaceFirst("[?]", resourceName);
		SQLQuery query = this.currentSession().createSQLQuery(sql);
		return query.list();
	}


}
