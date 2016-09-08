package com.funtest.analysis.dao;

import com.funtest.analysis.bean.Report;

public interface ReportDao {
    public Report queryReport(Integer id);	    
    public Integer createReport(Report report);
    public void deleteReport(Report report);
    public void deleteReport(Integer id);
    public void updateReport(Report report);
}
