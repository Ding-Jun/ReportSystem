package com.funtest.analysis.service.impl;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.SimpleReport;
import com.funtest.analysis.dao.DataConfigDao;
import com.funtest.analysis.dao.ReportBuilder;
import com.funtest.analysis.dao.ReportDao;
import com.funtest.analysis.exception.PersistException;
import com.funtest.analysis.service.ReportService;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
	private static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	@Autowired 
	DataConfigDao configDao; 
	@Autowired
	ReportDao dao;
	
	public Integer createReport(DataInfo dataInfo){
		ReportBuilder rBuilder= new ReportBuilder();
		rBuilder.setDataConfig(configDao.queryDataConfig(1));
		Report report= rBuilder.buildReport(dataInfo);
		
		return dao.createReport(report);
		
	}
	public DataInfo createDataInfo(String[] fileNames, InputStream[] ins,String reportName,String chipName,Integer mode) throws IOException{
		ReportBuilder rBuilder= new ReportBuilder();
		rBuilder.setDataConfig(configDao.queryDataConfig(1));
		return rBuilder.buildDataInfo(fileNames, ins, reportName, chipName, mode);
	}
	
	public void deleteReport(Integer id){
		if(id==null){
			throw new PersistException("id不能为空");
		}
		dao.deleteReport(id);
	}
	public Report queryReport(Integer id){
		if(id==null){
			throw new PersistException("id不能为空");
		}
		return dao.queryReport(id);
	}
	public Page<SimpleReport> queryPage(Integer curPage,Integer pageSize){
		if(curPage==null || curPage<0){
			return null;
		}
		
		PageCondition pCondition=new PageCondition();
		pCondition.setCurPage(curPage);
		pCondition.setSort("ORDER BY `time`");
		pCondition.setOrder("DESC");
		if(pageSize !=null && pageSize >0){
			pCondition.setPageSize(pageSize);
		}
		return dao.queryPage(pCondition);
		
	}
	public boolean queryExists(String reportName){
		return dao.queryExists(reportName);
	}
}
