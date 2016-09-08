package com.funtest.analysis.service;

import java.math.BigInteger;
import java.util.List;

import com.funtest.analysis.bean.Admin;
import com.funtest.analysis.bean.Permission;
import com.funtest.analysis.bean.Resource;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

public interface AdminService {
	/**
	 * 创建管理员
	 * @param 
	 * @return	管理员ID
	 */
	public Integer createAdmin(Admin admin);
    
	public void createAdmin(String name, Integer[] permissionIds);
	/**
	 * 删除管理员
	 * @param admin
	 */
    public void deleteAdmin(Integer id);    
    
    /**
     * 更改管理员表内容
     * @param admin
     */
    public void updateAdmin(Admin admin);
    	
    /**
     * 查询管理员
     * @param id
     * @return
     */
    public Admin queryAdmin(Integer id);
    
    /**
     * 根据名字查询管理员
     * @param name
     * @return
     */
    public Admin queryAdminByName(String name);
    
    public boolean queryCanRegister(String name,Integer userId);
    /**
     *查询管理员数量
     * @return
     */
    public BigInteger queryAdminRecords();
    
    /**
     * 分页查询
     * @param page
     * @param pageCondition
     * @return
     */
    public Page<Admin> queryPage(Page<Admin> page,PageCondition pageCondition);

    public String queryAdminName();
    
    public List<Resource> queryAllResources();
    
    public List<Permission> queryAllPermissions();
    
    public List<String> queryResources();
    
    public List<Permission> queryPermissions();
    
    public List<String> queryAdminPermissionsByResource(String resourceName);
    
    /**
     * 模糊查詢用戶
     * @param regionName
     * @param name 
     * @param curPage 
     * @param pageSize 
     * @return 用户页
     */
    public Page<Admin> queryAdminByNameHazily(String name, Integer curPage, Integer pageSize);

	void updateAdmin(Integer curUserId, String name, Integer[] permissionIds);
	
	public List<String> queryAdminPermissionsForShiro(Integer adminId) ;
	
	public String updateAdminPassword(Integer curUserId,String oldPassword,String newPassword);
}
