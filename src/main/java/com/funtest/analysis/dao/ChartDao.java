package com.funtest.analysis.dao;

import com.funtest.analysis.bean.Chart;

public interface ChartDao {
    public Chart queryChart(Integer id);	    
    public Integer createChart(Chart chart);
    public void deleteChart(Chart chart);
    public void deleteChart(Integer id);
    public void updateChart(Chart chart);
}
