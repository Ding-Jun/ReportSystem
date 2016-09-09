package com.funtest.analysis.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.funtest.analysis.bean.DataConfig;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.DataConfigDao;

@Repository
public class DataConfigImpl extends BaseDao<DataConfig> implements DataConfigDao{
	public DataConfigImpl() {
		// TODO Auto-generated constructor stub
	}
	
   public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
    public SessionFactory getSessionFactory() {
        return super.getSessionFactory();
    }
    
    public Session currentSession(){  
        return this.getSessionFactory().getCurrentSession();  
    }
	    
	@Override
	public Integer createDataConfig(DataConfig dataConfig) {
		// TODO Auto-generated method stub
		return this.create(dataConfig);
	}

	@Override
	public void deleteDataConfig(DataConfig dataConfig) {
		// TODO Auto-generated method stub
		this.delete(dataConfig);
	}

	@Override
	public void updateDataConfig(DataConfig dataConfig) {
		// TODO Auto-generated method stub
		this.update(dataConfig);
	}

	@Override
	public DataConfig queryDataConfig(Integer id) {
		// TODO Auto-generated method stub
		return this.query(id);
	}

}
