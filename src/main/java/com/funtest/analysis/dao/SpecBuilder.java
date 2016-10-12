package com.funtest.analysis.dao;

import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.ReportItem;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * spec
 *
 * @author admin
 * @create 2016-10-12 9:57
 */
public class SpecBuilder {
    /**
     * 返回 串串
     * @param r
     * @param out
     * @return
     * @throws IOException
     */
    public String creatSpec(Report r, OutputStream out) throws IOException {
        DecimalFormat nFormat = new DecimalFormat("#.0000");

        StringBuilder sb=new StringBuilder();
        List<ReportItem> ris = r.getReportItems();
        //write Header
        String endChar="\n";
        String splitChar=",";
        sb.append("Name:"+r.getFinalName()+endChar);
        sb.append("Date:"+new Date()+endChar);
        sb.append(endChar);

        sb.append("TestNo"+splitChar);
        sb.append("TestItem"+splitChar);

        sb.append("Sigma"+splitChar);
        sb.append("6Sigma-Min"+splitChar);
        sb.append("Typ"+splitChar);
        sb.append("6Sigma-Max"+splitChar);

        sb.append("Limit-Min"+splitChar);
        sb.append("Limit-Max"+splitChar);
        sb.append("Limit-Unit"+splitChar);
        sb.append("QA-Min"+splitChar);
        sb.append("QA-Max"+splitChar);

        sb.append("Datasheet-Min"+splitChar);
        sb.append("Datasheet-Max"+endChar);
        //Write Body
        String downFilename=r.getFinalName()+"_Spec.csv";

        for(ReportItem ri:ris){
            sb.append(ri.getTestNo()+splitChar);
            sb.append(ri.getColumnName()+splitChar);

            double sigma=ri.getPassChart().getStdev();
            double avg=ri.getPassChart().getRealAverage();
            double sigmaMin=avg-sigma*6;
            double sigmaMax=avg+sigma*6;
            sb.append(nFormat.format(sigma)+splitChar);
            sb.append(nFormat.format(sigmaMin) +splitChar);
            sb.append(nFormat.format(avg)+splitChar);
            sb.append(nFormat.format(sigmaMax)+splitChar);
            sb.append(ri.getLimitMin()+splitChar);
            sb.append(ri.getLimitMax()+splitChar);
            sb.append(ri.getLimitUnit()+endChar);
        }

        return sb.toString();

    }
}
