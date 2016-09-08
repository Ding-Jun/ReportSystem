package com.funtest.analysis.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDao<T> { 
		Logger logger = LoggerFactory.getLogger(this.getClass());
	   @Autowired	
	   private SessionFactory sessionFactory;
	   @SuppressWarnings("rawtypes")
	private Class entityClass;
	   
	   @SuppressWarnings("rawtypes")
	   public BaseDao(){
	    	Type genType = getClass().getGenericSuperclass();
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	        entityClass = (Class) params[0];
	    }
	   
	   public void setSessionFactory(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
	    public SessionFactory getSessionFactory() {
	        return sessionFactory;
	    }
	    
	    public Session currentSession(){  
	        return this.sessionFactory.getCurrentSession();  
	    }  
	    
	    @SuppressWarnings("unchecked")
		public T query(Integer id){
	    	long startTime = System.currentTimeMillis();
	    	T entity = (T)this.currentSession().get(entityClass, id);
	    	
	    	long endtime = System.currentTimeMillis();
	    	long durTime = endtime-startTime;
	    	logger.info("this hibernate opration costs time: {}.",durTime);
	    	return entity;
	    }
	    
	    public Integer create(T entity){
	    	long startTime = System.currentTimeMillis();
	    	Integer id = (Integer) this.currentSession().save(entity);
	    	long endtime = System.currentTimeMillis();
	    	long durTime = endtime-startTime;
	    	logger.info("this hibernate opration costs time: {}.",durTime);
			return id;
	    }
	    
	    public void update(T entity){
	    	long startTime = System.currentTimeMillis();
	    	this.currentSession().update(entity);
	    	long endtime = System.currentTimeMillis();
	    	long durTime = endtime-startTime;
	    	logger.info("this hibernate opration costs time: {}.",durTime);
	    }
	    
	    public void delete(T entity){
	    	long startTime = System.currentTimeMillis();
	    	this.currentSession().delete(entity);
	    	long endtime = System.currentTimeMillis();
	    	long durTime = endtime-startTime;
	    	logger.info("this hibernate opration costs time: {}.",durTime);
	    }

	    
	}