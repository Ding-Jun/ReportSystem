package com.funtest.analysis.dao.impl;

import com.funtest.analysis.bean.ReportItem;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.ReportItemDao;

public class ReportItemDaoImpl extends BaseDao<ReportItem> implements ReportItemDao{

	@Override
	public ReportItem queryReportItem(Integer id) {
		return super.query(id);
	}

	@Override
	public Integer createReportItem(ReportItem reportItem) {
		return super.create(reportItem);
	}

	@Override
	public void deleteReportItem(ReportItem reportItem) {
		System.out.println("virtual delete");
	}

	@Override
	public void deleteReportItem(Integer id) {
		System.out.println("virtual delete");
	}

	@Override
	public void updateReportItem(ReportItem reportItem) {
		super.update(reportItem);
	}

}
