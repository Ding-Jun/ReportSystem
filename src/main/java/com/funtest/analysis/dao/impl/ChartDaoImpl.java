package com.funtest.analysis.dao.impl;

import org.springframework.stereotype.Repository;

import com.funtest.analysis.bean.Chart;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.ChartDao;


@Repository
public class ChartDaoImpl extends BaseDao<Chart> implements ChartDao{

	@Override
	public Chart queryChart(Integer id) {
		
		return super.query(id);
	}

	@Override
	public Integer createChart(Chart chart) {
		return super.create(chart);
	}

	@Override
	public void deleteChart(Chart chart) {
		System.out.println("virtual delete");
	}

	@Override
	public void deleteChart(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateChart(Chart chart) {
		// TODO Auto-generated method stub
		
	}
	
}
