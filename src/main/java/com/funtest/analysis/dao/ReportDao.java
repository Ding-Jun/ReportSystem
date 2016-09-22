package com.funtest.analysis.dao;

import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.SimpleReport;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

public interface ReportDao {
    public Report queryReport(Integer id);	    
    public Integer createReport(Report report);
    public void deleteReport(Report report);
    public void deleteReport(Integer id);
    public void updateReport(Report report);
    public Page<SimpleReport> queryPage(PageCondition pCondition);
    public boolean queryExists(String reportName);
}
