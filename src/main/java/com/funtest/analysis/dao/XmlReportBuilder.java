package com.funtest.analysis.dao;

import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.ReportItem;
import com.funtest.analysis.util.WordUtils;
import com.funtest.analysis.util.XmlUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.List;

/**
 * builder
 *
 * @author admin
 * @create 2016-10-11 19:03
 */
public class XmlReportBuilder {
    public static final String ONE_PX_IMG="Qk06AAAAAAAAADYAAAAoAAAAAQAAAAEAAAABABgAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAA////AA==";
    public Document createXmlReport(String templateName, Report report) throws DocumentException{
        Document doc=null;
        SAXReader reader=new SAXReader();
        try {
            doc = reader.read(new ClassPathResource( templateName).getFile());
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            throw new DocumentException(templateName+"未找到！");
        }catch (Exception e){
            e.printStackTrace();
        }
        //获取根节点元素对象
        Element root = doc.getRootElement();
        HashMap map = new HashMap();
        map.put("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main");
        XPath xpath =doc.createXPath( "//w:t");
        xpath.setNamespaceURIs(map);

        List<Element> list = xpath.selectNodes(doc );
        XmlUtils.replace(list,"chipName",report.getChipName());
        XmlUtils.replace(list,"finalName",report.getFinalName());
        XmlUtils.replace(list,"testCount",String.valueOf(report.getTestCount()));
        XmlUtils.replace(list,"lotNo",report.getLotNo());
        XmlUtils.replace(list,"sealNo",report.getSealNo());
        XmlUtils.replace(list,"passPercent",report.getPassPercent());
        XmlUtils.replace(list,"testMan",report.getTestMan());
        XmlUtils.replace(list,"packageStyle",report.getPackageStyle());
        XmlUtils.replace(list,"sufeng",report.getSufeng());
        XmlUtils.replace(list,"osFailCount",String.valueOf(report.getOsFailCount()));
        XmlUtils.replace(list,"osFailRate",report.getOsFailRate());
        xpath =doc.createXPath( "//w:body");
        xpath.setNamespaceURIs(map);
        Node body =xpath.selectSingleNode(doc);

        xpath =doc.createXPath( "//w:tbl");
        xpath.setNamespaceURIs(map);
        List<Element> tables = xpath.selectNodes(doc);
        Element parent = tables.get(0).getParent();


        //deal with table(0)
        List<Element> trList =tables.get(0).elements("tr");
        int testItemLine=0;
        for(int i=0;i<trList.size();i++){
            if(trList.get(i).element("tc").element("p").element("r").element("t").getTextTrim().equals("testNo")){
                testItemLine=i;

                break;
            }
        }
        //add row
        List<ReportItem> reportItems=report.getReportItems();
        for(int j=0;j<reportItems.size();j++){
            Element trCopy=trList.get(testItemLine).createCopy();
            List<Element> textList=XmlUtils.findElements(trCopy, "tc/p/r/t");

            //fill Row
            XmlUtils.replace(textList,"testNo",String.valueOf(reportItems.get(j).getTestNo()));
            XmlUtils.replace(textList,"columnname",reportItems.get(j).getColumnName());
            XmlUtils.replace(textList,"failCount",String.valueOf(reportItems.get(j).getFailCount()));
            XmlUtils.replace(textList,"failRate",reportItems.get(j).getFailRate());




            trList.add(testItemLine+(1+j), trCopy);
        }
        //remove template
        trList.remove(testItemLine);
        //remove blank
        int removeLines=reportItems.size()%44+44;

        for(int k=0;k<removeLines;k++){
            if(((Element)body).elements("p").size()>10){
                ((Element)body).elements("p").remove(10);
            }

        }

        //deal with table(1)

        for(ReportItem ri:reportItems){


            //add table
            Element testItemCopy=tables.get(1).createCopy();


            //List<Element> tiList= xpath.selectNodes(testItem.getDocument());
            //List<Element> tiList= testItem.selectNodes("//w:t");

            //method3 test  ok
            List<Element> tList=XmlUtils.findElements(testItemCopy, "tr/tc/p/r/t");

            XmlUtils.replace(tList,"testNo",String.valueOf(ri.getTestNo()));
            XmlUtils.replace(tList,"columnname",ri.getColumnName());
            XmlUtils.replace(tList,"limit","("+ri.getLimitMin()+","+ri.getLimitMax()+")"+ri.getLimitUnit());
            XmlUtils.replace(tList,"raphComment","此项失效了"+ri.getFailCount()+"颗");
            //System.out.println("tiList size:"+tList.size()+"   "+ri.getColumnname());
            //System.out.println(Arrays.toString(testItem.elements().toArray()));



            //fill blank
            int lenth=44;
            for(int j=0;j<lenth;j++){
                Element blank=parent.addElement("w:p");
                blank.addAttribute("w:rsidP", "000E1CD1");
                blank.addAttribute("w:rsidRDefault", "000E1CD1");
                blank.addAttribute("w:rsidR", "000E1CD1");
            }
            //replace img id
            List<Element> imgList =XmlUtils.findElements(testItemCopy, "tr/tc/p/r/pict/shape/imagedata");
            //System.out.println("image list:size"+imgList.size());
            imgList.get(0).attribute(0).setData("imgId"+ri.getTestNo()*2);
            WordUtils.reportImageBind(doc,"passChart"+ri.getTestNo()+".png","imgId"+ri.getTestNo()*2);
            imgList.get(1).attribute(0).setData("imgId"+ri.getTestNo()*2+1);

            WordUtils.reportImageBind(doc,"failChart"+ri.getTestNo()+".png","imgId"+ri.getTestNo()*2+1);
            if(ri.getPassChart()!=null){
                //add img
                WordUtils.reportAddImage(doc,"passChart"+ri.getTestNo()+".png",ri.getPassChart().getChartImg());
            }else{
                //add 1px mock img
                WordUtils.reportAddImage(doc,"passChart"+ri.getTestNo()+".png",ONE_PX_IMG);
            }

            if(ri.getFailChart()!=null){
                WordUtils.reportAddImage(doc,"failChart"+ri.getTestNo()+".png",ri.getFailChart().getChartImg());
            }else {
                WordUtils.reportAddImage(doc,"failChart"+ri.getTestNo()+".png",ONE_PX_IMG);
            }

            //imgList.get(0).addAttribute("r:id", "imgId"+ri.getTestNo()*2);
            //imgList.get(1).addAttribute("r:id", "imgId"+ri.getTestNo()*2+1);
			/*
			//add img
			WordUtils.reportAddImage(doc,ri.getColumnname()+"passChart.png",ri.getPassChart().getChartImg());
			WordUtils.reportAddImage(doc,ri.getColumnname()+"failChart.png",ri.getFailChart().getChartImg());
			//bind
			WordUtils.reportImageBind(doc,ri.getColumnname()+"passChart.png","imgId"+ri.getTestNo()*2);
			WordUtils.reportImageBind(doc,ri.getColumnname()+"failChart.png","imgId"+ri.getTestNo()*2+1);
			*/
            parent.elements().add(testItemCopy);
        }
        //remove reportItemTemplate
        if(tables.get(1).getParent().remove(tables.get(1))){
            System.out.println("template removed");

        }
        removeLines=44;

        for(int k=0;k<removeLines;k++){

            ((Element)body).elements("p").remove(10);
        }

        //System.out.println("tables size"+parent.elements().size());
        return doc;
    }
}
