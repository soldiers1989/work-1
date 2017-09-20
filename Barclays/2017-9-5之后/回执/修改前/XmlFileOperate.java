package com.aif.rpt.biz.aml.receipt.server;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aif.rpt.biz.aml.receipt.server.RetXmlFileImpService.RetXmlType;
import com.allen_sauer.gwt.log.client.Log;

public class XmlFileOperate {
	private DocumentBuilder dcb = null;
	private Document dc = null;
	private DocumentBuilder dcb1 = null;
	private Document dc1 = null;
	private Transformer transformer = null;
	private static String encodeing="GB18030";
	private List<String> fileContentList = new ArrayList();
	
	public boolean init(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(true);
		try{
			dcb=dbf.newDocumentBuilder();
			dc = dcb.newDocument();
			TransformerFactory factory = TransformerFactory.newInstance();
			transformer = factory.newTransformer();
		}catch(Exception e){
			Log.error("XML对象初始化："+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 *  函数名称  newNode
	 *  功能      创建新节点
	 *  输入      nodeName-新节点名称 nodeValue-节点值
	 *  输出      成功返回新建节点，否则返回null
	 */	
	public Element newNode(String nodeName,String nodeValue){
		try{
			Element node = dc.createElement(nodeName);			
			node.setTextContent(nodeValue);
			return node;
		}catch(Exception e){
			Log.error("newNode："+nodeName+e.getMessage());
			return null;
		}		
	}
	
	/**
	 *  函数名称  newAttr
	 *  功能      创建新属xing性
	 *  输入      nodeName-新节点名称 nodeValue-节点值
	 *  输出      成功返回新建节点，否则返回null
	 */	
	public Attr newAttr(String attrName,String attrValue){
		try{
			Attr node = dc.createAttribute(attrName);	
			node.setNodeValue(attrValue);
			return node;
		}catch(Exception e){
			Log.error("newAttr："+attrName+e.getMessage());
			return null;
		}
		
	}
	/**
	 *  函数名称  addNode
	 *  功能      增加叶节点
	 *  输入      pNode-父节点 node-新节点
	 *  输出      true-success  false-fail
	 */	
	public boolean addNode(Element pNode,Element node){
		try{			
			if (pNode!=null){
				pNode.appendChild(node);
			}else{
				//增加新的根节?
				dc.appendChild(node);
			}
			
			return true;
		}catch(Exception e){
			Log.error("addNode：" + e.getMessage());
			return false;
		}		
	}
	
	/**
	 *  函数名称  addAttribute
	 *  功能      增加属性
	 *  输入      pNode-当前节点 newAttr-属性对象
	 *  输出      true-success  false-fail
	 */	
	public boolean addAttribute(Element pNode,Attr newAttr){
		try{		
			pNode.setAttributeNode(newAttr);
			return true;
		}catch(Exception e){
			Log.error("addAttribute：" + e.getMessage());
			return false;
		}	
	}
	
	
	/**
	    * 将xml对象转化为字符串
	    * @param document 
	    * @return
	    */
	private String getDocumentString(Document document) {
		String resultString = "";
	    try {
	    	//XML默认编码为UTF-8，此处改为GB18030
            //transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "gbk");
            transformer.setOutputProperty("encoding", encodeing);  
	    	StringWriter write = new StringWriter();
	    	DOMSource ds=new DOMSource(document);
	    	StreamResult sr=new StreamResult(write);
	    	System.out.println("document:"+document);
	    	System.out.println("DOMSource:"+ds);
	    	
	    	transformer.transform(ds, sr);
	    	
	    	resultString = write.toString();	    	
	    	//System.out.print(resultString);
	    }catch (Exception e) {
	    	Log.error("将xml对象转化为字符串：" + e);	   
	    	e.printStackTrace();
	    }
	    return resultString;
	}

	/**
	 *  函数名称  saveXmlFile
	 *  功能      保存文件至xml文件
	 *  输入      
	 *  输出      true-success  false-fail
	 */	
	public boolean saveXmlFile(String fileFullName){
		//文件输出流
        FileOutputStream fos = null;
        //数据输出流
        OutputStreamWriter dos = null;
        try{
        	File file = new File(fileFullName);
        	if(file.exists()){
        		file.delete();
        	}
        	file.createNewFile();
        	System.out.println("dc-----"+dc);
        	String s = getDocumentString(dc);
        	s = s.replaceFirst(" standalone=\"no\"", "");
        	fos = new FileOutputStream(file);
            dos = new OutputStreamWriter(fos,encodeing);
            dos.write(s);
            dos.flush();
            dos.close();
            fos.close();
            return true;
        }catch(Exception e){
        	Log.error("保存文件至xml文件：" + e.getMessage());
        	return false;
        }
	}	
	
	/**
	 *  函数名称  LoadXmlFile
	 *  功能      载入xml文件
	 *  输入      
	 *  输出      true-success  false-fail
	 */	
	public boolean LoadXmlFile(String fileFullName,String path){
		try
		{

			File vFile = new File(path+"tmp.xml");
			if (!vFile.exists()){
				Log.error(fileFullName+"转换失败");
				return false;
			}	
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			InputStream is=new FileInputStream(path+"tmp.xml");
			dbf.setNamespaceAware(false);
			dbf.setValidating(false);	
			dcb1=dbf.newDocumentBuilder();
			dc1=dcb1.parse(is);
			//dc=dcb.parse(vFile);
			return true;
		}catch(Exception e){
			Log.error("载入xml文件"+e.getMessage());
			e.printStackTrace();
			return false;
		}		
	}
	
	/**
	 *  函数名称  getNodeValue
	 *  功能      根据XPath取值
	 *  输入      
	 *  输出      true-success  false-fail
	 */	
	public String getNodeValue(String pathExpression){
		XPath xpath = XPathFactory.newInstance().newXPath();
		try{
			XPathExpression expr = xpath.compile(pathExpression);
			Node tnd = (Node)expr.evaluate(dc1,XPathConstants.NODE);
			String tmp = (String)expr.evaluate(dc1,XPathConstants.STRING);
			return tmp;
		}catch(Exception e){
			Log.error(pathExpression+"读取失败");
			return "";
		}
	}
	
	public String getNodeValue(String pathExpression, Node node){
		XPath xpath = XPathFactory.newInstance().newXPath();
		try{
			
			Node retNode = (Node) xpath.evaluate(pathExpression, node, XPathConstants.NODE);
			String retValue = (String) xpath.evaluate(pathExpression, node, XPathConstants.STRING);
			return retValue;
		}catch(Exception e){
			Log.error(pathExpression+"读取失败");
			return "";
		}
	}
	
	public NodeList getNodeList(String pathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try{
			XPathExpression expr = xpath.compile(pathExpression);
			NodeList nodeList = (NodeList)expr.evaluate(dc1,XPathConstants.NODESET);
			return nodeList;
		}catch(Exception e){
			Log.error(pathExpression+"读取失败");
			return null;
		}
	}
	
	public NodeList getNodeList(String pathExpression, Node node) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try{
			NodeList nodeList = (NodeList) xpath.evaluate(pathExpression, node, XPathConstants.NODESET);
			return nodeList;
		}catch(Exception e){
			Log.error(pathExpression+"读取失败");
			return null;
		}
	}
	
	/**
	 * 读取导入文件内容
	 * 
	 * @param sFileFullName文件路径名称
	 * 
	 */
	public boolean transFormXmlFile(String fileFullName,String path) {
		
		try{
			String line;
			fileContentList.clear();
			
			String encode = "gbk";
			InputStream is = new FileInputStream(fileFullName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"GBK"));
			try {
				line = reader.readLine();            
				while (line != null) {					
					fileContentList.add(line);
					line = reader.readLine();
				}		
				
				return saveToNewFile(path);
			} finally {
				is.close();
				reader.close();
			}
		}catch(Exception e){
			Log.error(fileFullName+"读取失败");
			return false;
		}

	}
	
	/**
	 * 写文件内容
	 * 
	 * @param sFileFullName文件路径名称
	 * @throws IOException
	 */
	public boolean saveToNewFile(String path){
		//文件输出流
        FileOutputStream fos = null;
        //数据输出流
        OutputStreamWriter dos = null;
        try{
        	File file = new File(path+"tmp.xml");
        	if(file.exists()){
        		file.delete();
        	}
        	file.createNewFile();
        	        	
        	String s = "";
        	for(int i=0;i<fileContentList.size();i++){
        		s = s+fileContentList.get(i).trim();
        	}
        	s = s.replace("utf-8", "utf-8");
        	fos = new FileOutputStream(file);
            dos = new OutputStreamWriter(fos,"GBK");
            dos.write(s);
            dos.flush();
            dos.close();
            fos.close();
            return true;
        }catch(Exception e){
        	Log.error("写文件内容"+e.getMessage());
        	return false;
        }
	}
	
	public static void main(String[] args) {
	
		XmlFileOperate xmlOp = new XmlFileOperate();
		if (!xmlOp.transFormXmlFile("D:/FDRC[NBH010093100009925-20150417-0001-0001]20150417153344.XML", "D:/")) {
			System.out.println("aaa");
		}
		if (!xmlOp.LoadXmlFile("D:/FDRC[NBH010093100009925-20150417-0001-0001]20150417153344.XML", "D:/")) {
			System.out.println("bbb");
		}
		
		
		String expression;
		String exprRCLC; // 补正定位xpath
		String sRCLC; // 补正定位信息
		String exprRCSG; // 补正提示xpath
		String sRCSG; // 补正提示
		String sNodeName; // 错误节点名
		// 读补正要求总数
					String exprRCTN = "/FDBK/RCTN";
					String preExpr = "/FDBK/FCRCs/FCRC[";
					String affExpr = "]";
					int iRCTN = Integer.valueOf(xmlOp.getNodeValue(exprRCTN));
					// 大额
					for (int i = 1; i <= iRCTN; i++) {
						expression = preExpr + String.valueOf(i) + affExpr
								+ "/RCIFs/RCIF[";
						sRCLC = "test";
						int j = 0;
						while (sRCLC != null && sRCLC.length() > 0) {
							j++;
							exprRCLC = expression + String.valueOf(j) + affExpr;
							exprRCLC = exprRCLC + "/RCLC";
							sRCLC = xmlOp.getNodeValue(exprRCLC);
							exprRCSG = expression + String.valueOf(j) + affExpr
									+ "/RCSG";
							sRCSG = xmlOp.getNodeValue(exprRCSG);			
			
							
							// 更新回执信息到分析结果表
							Map parameter = new HashMap();
							parameter.put("RPT_STATUS", "20"); // 上报成功 解析补正定位时需要设置为22(要求补正)
							parameter.put("REPORT_TYPE", "N"); // 普通 解析补正定位时需要设置为I(补正) 
							parameter.put("RPT_STATUS2", "11"); // 已上报
							
							// 更新上报文件表
							parameter = new HashMap();
							parameter.put("FILE_STATUS", "12"); // 补正
							parameter.put("FILE_STATUS2", "00"); // 正常
							// 插入回执日志表
							parameter = new HashMap();
							parameter.put("FILE_TYPE", "02"); // 要求补正回执
							parameter.put("ERROR_POINTER", sRCLC);
							parameter.put("ERROR_INFORMATION", sRCSG);
							
							if ((sRCLC != null) && (sRCLC.length() > 4)) {
								sNodeName = sRCLC.substring(sRCLC.length() - 4);
							} else {
								sNodeName = "";
							}
							
						}
					}
		
	}
	
	
	
}
