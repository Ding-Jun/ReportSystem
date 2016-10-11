package com.funtest.analysis.util;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public class WordUtils {

	
	public static void reportAddImage(Document doc,String imgName,String binaryData){
		Element img = doc.getRootElement().addElement("pkg:part");
		img.addAttribute("pkg:contentType", "image/png"	);
		img.addAttribute("pkg:name", "/word/media/"+imgName);
		img.addAttribute("pkg:compression", "store");
		Element data =img.addElement("pkg:binaryData");
		data.addText(binaryData);
	}
	
	public static void reportImageBind(Document doc,String imgName,String imgId){
		List<Element> list = XmlUtils.findElements(doc.getRootElement(), "part/xmlData/Relationships");
		Element r = list.get(1).addElement("Relationship");
		r.addAttribute("Target", "media/"+imgName);
		r.addAttribute("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image");
		r.addAttribute("Id", imgId);
		//System.out.println(list.size());
	}

}
