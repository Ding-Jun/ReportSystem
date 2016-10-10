package com.funtest.analysis.dao.impl;

import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.SimpleReport;
import com.funtest.analysis.dao.BaseDao;
import com.funtest.analysis.dao.ReportDao;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class ReportDaoImpl extends BaseDao<Report> implements ReportDao{

	private static final String DELETE_REPORT="UPDATE t_report SET `isDeleted`=true where id=? " ;
	private static final String QUERY_RECORDS="SELECT COUNT(*) FROM v_report Where `isDeleted`=false ";
	private static final String QUERY_PAGE="SELECT * FROM v_report WHERE `isDeleted`=false ";
	private static final String QUERY_REPORT="SELECT * FROM t_report WHERE `isDeleted`=false AND `id`=? ";
	private static final String QUERY_REPORT_EXISTS="SELECT COUNT(*) FROM t_report WHERE `isDeleted`=false AND `reportName`='?' ";
	@Override
	public Report queryReport(Integer id) {
		String sql=QUERY_REPORT.replaceFirst("[?]", id.toString());
		return (Report) currentSession().createSQLQuery(sql).addEntity(Report.class).uniqueResult();
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
		String sql=DELETE_REPORT.replaceFirst("[?]", id.toString());
		super.currentSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public void updateReport(Report report) {
		super.update(report);
		
	}
	
	public Page<SimpleReport> queryPage(PageCondition pCondition){

		Long records=((BigInteger)super.currentSession().createSQLQuery(QUERY_RECORDS).uniqueResult()).longValue();
		int pageSize=pCondition.getPageSize();
		int totalPages=(int)Math.ceil(records/(double)pageSize);
		int curPage=pCondition.getCurPage();
		if(curPage<=0) curPage=1;
		if(curPage>totalPages) curPage=totalPages;
		String sql =QUERY_PAGE+" "+pCondition.getSort()+" "+pCondition.getOrder();
		SQLQuery query =currentSession().createSQLQuery(sql);
		query.setFirstResult((curPage-1)*pageSize);
		query.setMaxResults(pCondition.getPageSize());
		
		Page<SimpleReport> page=new Page<SimpleReport>();
		page.setTotalPage(totalPages);
		page.setCurPage(curPage);
		page.setPageSize(pageSize);
		page.setTotalRows(records);
		page.setRowData(query.addEntity(SimpleReport.class).list());
		return page;
		
	}

	public boolean queryExists(String reportName){
		String sql=QUERY_REPORT_EXISTS.replaceFirst("[?]", reportName);
		long records =((BigInteger) super.currentSession().createSQLQuery(sql).uniqueResult()).longValue();
		return records>0;
		
	}
}
