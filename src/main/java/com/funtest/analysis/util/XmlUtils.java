package com.funtest.analysis.util;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlUtils {
	public static int  replace(List<Element> l,String srcStr,String replaceStr){
		int occurCnt=0;
		Iterator<Element> iter=l.iterator();
        while(iter.hasNext()){
        	Element e=iter.next();
        	if(e.getTextTrim().equals(srcStr)){
        		occurCnt++;
        		e.setText(replaceStr);
        	}
        }
        return occurCnt;
	}
	
	public static List<Element> findElements(Element root,String expression){
		
		String[] elements = expression.split("/");
		if(elements.length == 0){
			return null;
		}
		List<Element> list=root.elements(elements[0]);
		for(int i=1;i<elements.length ;i++){
			list=findElements(list, elements[i]);
		}
		
		
		
		return list;
	}
	
	public static List<Element> findElements(List<Element> roots,String expression){
		List<Element> list=new ArrayList<Element>();
		
		Iterator<Element> iter= roots.iterator();
		while(iter.hasNext()){
			List<Element> curList=iter.next().elements(expression);
			if(curList !=null){
				list.addAll(curList);
			}
		}
			
		return list;
	}
}
