package com.funtest.analysis.dao.impl;

import com.funtest.analysis.bean.Report;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.ReportDao;

public class ReportDaoImpl extends BaseDao<Report> implements ReportDao{

	@Override
	public Report queryReport(Integer id) {
		return super.query(id);
	}

	@Override
	public Integer createReport(Report report) {
		return super.create(report);
	}

	@Override
	public void deleteReport(Report report) {
		System.out.println("virtual delete");
	}

	@Override
	public void deleteReport(Integer id) {
		// TODO Auto-generated method stub
		System.out.println("virtual delete");
	}

	@Override
	public void updateReport(Report report) {
		super.update(report);
		
	}

}
