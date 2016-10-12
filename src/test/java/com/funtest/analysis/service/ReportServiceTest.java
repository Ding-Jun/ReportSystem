package com.funtest.analysis.service;


import com.funtest.analysis.bean.Report;
import com.funtest.analysis.service.impl.ReportServiceImpl;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;


/**
 * test
 *
 * @author admin
 * @create 2016-10-11 18:21
 */
public class ReportServiceTest {
    @Test
    public void testDownloadXmlReport() throws IOException {
        ReportService service=new ReportServiceImpl();
        Resource resource=new ClassPathResource("report.json");
        BufferedReader reader= new BufferedReader(new FileReader(resource.getFile()));
        StringBuilder reportJson=new StringBuilder();
        String line;
        while((line=reader.readLine())!=null){
            reportJson.append(line);
        }
        Report report=new Gson().fromJson(reportJson.toString(),Report.class);
        OutputStream out = new FileOutputStream(new File(Constants.FILE_UPLOAD_DIR+"/report1011.xml"));
        service.downloadReport(report,"xml",out);

        //service.downloadReport()
        Assert.assertEquals(1,1);
    }
    @Test
    public void testDownloadSpec() throws IOException {
        ReportService service=new ReportServiceImpl();
        Resource resource=new ClassPathResource("report.json");
        BufferedReader reader= new BufferedReader(new FileReader(resource.getFile()));
        StringBuilder reportJson=new StringBuilder();
        String line;
        while((line=reader.readLine())!=null){
            reportJson.append(line);
        }
        Report report=new Gson().fromJson(reportJson.toString(),Report.class);

        OutputStream out = new FileOutputStream(new File(Constants.FILE_UPLOAD_DIR+"/spec1011.csv"));
        service.downloadReport(report,"spec",out);
    }
}
