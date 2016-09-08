package com.funtest.analysis.dao;

import java.math.BigInteger;
import java.util.List;

import com.funtest.analysis.bean.Resource;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

public interface ResourceDao {
	/**
	 * 创建资源
	 * @param 
	 * @return	资源ID
	 */
	public Integer createResource(Resource resource);
    
	/**
	 * 删除资源
	 * @param resource
	 */
    public void deleteResource(Resource resource);    
    
    /**
     * 更改资源表内容
     * @param resource
     */
    public void updateResource(Resource resource);
    	
    /**
     * 查询资源
     * @param id
     * @return
     */
    public Resource queryResource(Integer id);
    
    /**
     * 根据名字查询资源
     * @param name
     * @return
     */
    public Resource queryResourceByName(String name);
    
    /**
     *查询资源数量
     * @return
     */
    public BigInteger queryResourceRecords();
    
    /**
     * 查询所有资源
     * @return 资源列表
     */
    public List<Resource> queryAllResources();
    
    /**
     * 分页查询
     * @param page
     * @param pageCondition
     * @return
     */
    public Page<Resource> queryPage(Page<Resource> page,PageCondition pageCondition);

    public List<String> queryResources(Integer adminId);
}
