package com.funtest.analysis.dao;

import com.funtest.analysis.bean.ReportItem;

public interface ReportItemDao {
    public ReportItem queryReportItem(Integer id);	    
    public Integer createReportItem(ReportItem reportItem);
    public void deleteReportItem(ReportItem reportItem);
    public void deleteReportItem(Integer id);
    public void updateReportItem(ReportItem reportItem);
}
