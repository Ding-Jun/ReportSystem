package com.funtest.analysis.dao;

import java.math.BigInteger;
import java.util.List;

import com.funtest.analysis.bean.Admin;
import com.funtest.analysis.bean.Permission;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

public interface AdminDao {
	/**
	 * 创建管理员
	 * @param 
	 * @return	管理员ID
	 */
	public Integer createAdmin(Admin admin);
    
	/**
	 * 删除管理员
	 * @param admin
	 */
    public void deleteAdmin(Admin admin);    
    
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

    public List<Permission> queryPermissions(Integer adminId);
    
    public List<String> queryAdminPermissionsForShiro(Integer adminId);
}
