package com.funtest.analysis.service.impl;

import com.funtest.analysis.bean.Admin;
import com.funtest.analysis.bean.Permission;
import com.funtest.analysis.bean.Resource;
import com.funtest.analysis.dao.AdminDao;
import com.funtest.analysis.dao.PermissionDao;
import com.funtest.analysis.dao.ResourceDao;
import com.funtest.analysis.util.CustomSessionUtil;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class AdminServiceImpl implements com.funtest.analysis.service.AdminService{

	@Autowired
	AdminDao adminDao;

	@Autowired
	PermissionDao permissionDao;
	
	@Autowired
	ResourceDao resourceDao;
	
	@Override
	public Integer createAdmin(Admin admin) {
		// TODO Auto-generated method stub
		return adminDao.createAdmin(admin);
	}

	@Override
	public void createAdmin(String name, Integer[] permissionIds) {
		Admin admin=new Admin();
		admin.setName(name);
		List<Permission> permissionList=new ArrayList<Permission>();
		if(permissionIds !=null){
			for(Integer id:permissionIds){
				Permission p=new Permission();
				p.setId(id);
				permissionList.add(p);
			}
		}
		admin.setPermissions(permissionList);
		admin.setPassword("1");
		adminDao.createAdmin(admin);
	}
	
	@Override
	public void deleteAdmin(Integer id) {
		Admin admin= adminDao.queryAdmin(id);
		adminDao.deleteAdmin(admin);
	}

	@Override
	public void updateAdmin(Admin admin) {
		adminDao.updateAdmin(admin);
	}

	@Override
	public Admin queryAdmin(Integer id) {
		return adminDao.queryAdmin(id);
	}

	@Override
	public Admin queryAdminByName(String name) {
		return adminDao.queryAdminByName(name);
	}

	@Override
	public BigInteger queryAdminRecords() {
		return adminDao.queryAdminRecords();
	}

	@Override
	public Page<Admin> queryPage(Page<Admin> page, PageCondition pageCondition) {
		return adminDao.queryPage(page, pageCondition);
	}

	@Override
	public List<Resource> queryAllResources() {
		return resourceDao.queryAllResources();
	}

	@Override
	public List<Permission> queryAllPermissions() {
		return permissionDao.queryAllPermissions();
	}

	@Override
	public List<String> queryResources() {
		Integer adminId= CustomSessionUtil.getLoginAdminId();
		return resourceDao.queryResources(adminId);
	}

	@Override
	public List<Permission> queryPermissions() {
		Integer adminId=CustomSessionUtil.getLoginAdminId();
		return adminDao.queryPermissions(adminId);
	}

	@Override
	public String queryAdminName() {
		return CustomSessionUtil.getLoginAdminName();
	}

	@Override
	public List<String> queryAdminPermissionsByResource(String resourceName) {
		Integer adminId=CustomSessionUtil.getLoginAdminId();
		if(adminId == null){
			throw new RuntimeException("用户未登陆");
		}
		return permissionDao.queryAdminPermissionsByResource(adminId, resourceName);
	}

	@Override
	public Page<Admin> queryAdminByNameHazily(String name, Integer curPage, Integer pageSize) {
		//PageCondition
		PageCondition pCondition=new PageCondition();;
		if("*".equals(name)){
			name="";
		}
		String condition="AND `name` LIKE '%"+name+"%'";
		pCondition.setOrder(condition);
		pCondition.setSort("ORDER BY 'id' ASC ");
		//Page
		Page<Admin> page=new Page<Admin>();
		page.setCurPage(curPage);
		page.setPageSize((pageSize ==null)?5:pageSize);
		return adminDao.queryPage(page, pCondition);
	}

	@Override
	public void updateAdmin(Integer curUserId, String name, Integer[] ids) {
		if(curUserId == null){
			throw new RuntimeException("用户未登陆");
		}
		Admin admin=adminDao.queryAdmin(curUserId);
		if(admin !=null){
			if(name!= null &&name!=""){
				admin.setName(name);
			}
			List<Permission> permissionList=new ArrayList<Permission>();
			if(ids !=null){
				for(Integer id:ids){
					Permission p=new Permission();
					p.setId(id);
					permissionList.add(p);
				}
			}
			admin.setPermissions(permissionList);
			adminDao.updateAdmin(admin);
		}
		
	}

	@Override
	public List<String> queryAdminPermissionsForShiro(Integer adminId) {
		return adminDao.queryAdminPermissionsForShiro(adminId);
	}

	@Override
	public String updateAdminPassword(Integer curUserId, String oldPassword, String newPassword) {
		System.out.println("curUserId "+curUserId);
		if(curUserId == null){
			return null;
		}
		Admin admin = adminDao.queryAdmin(curUserId);
		
		if(oldPassword ==null ||admin.getPassword().equals(oldPassword)){
			admin.setPassword(newPassword);
		}else { 
			return null;
		}
		adminDao.updateAdmin(admin);
		return newPassword;
	}

	@Override
	public boolean queryCanRegister(String name,Integer userId) {
		Admin admin = adminDao.queryAdminByName(name);
		if(admin == null){
			return true;
		}
		return userId== null?admin == null:admin.getId()==userId;
	}



	
	
}
