package com.funtest.analysis.dao;

import org.hibernate.Session;

import com.funtest.analysis.bean.DataConfig;
/**
 * 
 * @author admin	2016年8月5日14:32:09
 *
 */
public interface DataConfigDao {
	/**
	 * 创建配置
	 * @param dataConfig
	 * @return 配置id
	 */
    public Integer createDataConfig(DataConfig dataConfig);
    
    /**
     * 删除配置
     * @param dataConfig
     */
    public void deleteDataConfig(DataConfig dataConfig);
    
    /**
     * 更新配置
     * @param dataConfig
     */
    public void updateDataConfig(DataConfig dataConfig);
    
    /**
     * 查询配置
     * @param id
     * @return 配置
     */
    public DataConfig queryDataConfig(Integer id);
    
    /**
     * 获取Session
     * @return Session
     */
    public Session currentSession();
}
