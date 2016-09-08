package com.funtest.analysis.dao;

import java.util.List;

import com.funtest.analysis.bean.Permission;

public interface PermissionDao{
	/**
	 * 创建权限
	 * @param 
	 * @return	权限ID
	 */
	public Integer createPermission(Permission permission);
    
	/**
	 * 删除权限
	 * @param permission
	 */
    public void deletePermission(Permission permission);    
    
    /**
     * 更改权限表内容
     * @param permission
     */
    public void updatePermission(Permission permission);
    	
    /**
     * 查询权限
     * @param id
     * @return
     */
    public Permission queryPermission(Integer id);
    
    /**
     * 根据名字查询权限
     * @param name
     * @return
     */
    public Permission queryPermissionByName(String name);
    
    /**
     *查询权限数量
     * @return
     */
    //public BigInteger queryPermissionRecords();
    
    
    public List<Permission> queryAllPermissions();
    
    public List<String> queryAdminPermissionsByResource(Integer adminId,String resourceName);
    /**
     * 分页查询
     * @param page
     * @param pageCondition
     * @return
     */
    //public Page<Permission> queryPage(Page<Permission> page,PageCondition pageCondition);
}
