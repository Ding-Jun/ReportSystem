package com.funtest.analysis.dao;

import java.math.BigInteger;

import com.funtest.analysis.bean.Role;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

public interface RoleDao {
	/**
	 * 创建角色
	 * @param 
	 * @return	角色ID
	 */
	public Integer createRole(Role Role);
    
	/**
	 * 删除角色
	 * @param role
	 */
    public void deleteRole(Role role);    
    
    /**
     * 更改角色表内容
     * @param role
     */
    public void updateRole(Role role);
    	
    /**
     * 查询角色
     * @param id
     * @return
     */
    public Role queryRole(Integer id);
    
    /**
     * 根据名字查询角色
     * @param name
     * @return
     */
    public Role queryRoleByName(String name);
    
    /**
     *查询角色数量
     * @return
     */
    public BigInteger queryRoleRecords();
    
    /**
     * 分页查询
     * @param page
     * @param pageCondition
     * @return
     */
    public Page<Role> queryPage(Page<Role> page,PageCondition pageCondition);

}
