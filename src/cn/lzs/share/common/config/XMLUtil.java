package cn.lzs.share.common.config;

import java.io.File;
import java.io.FileOutputStream;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLUtil {

	/**
	 * 获取 xml 文件中的 document
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Document getXMLDocument(String path) throws Exception{
		Document document=null;
		try{
			File configFile=new File(path);
			SAXReader saxReader=new SAXReader();
			document=saxReader.read(configFile);
		}catch(Exception e){
			throw e;
		}
		return document;
	}
	
	/**
	 * 获取一个 OutputFormat
	 * @return
	 */
	public static OutputFormat getOutputFormat(){
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();// 设置XML文档输出格式
		outputFormat.setEncoding("UTF-8");// 设置XML文档的编码类型
		outputFormat.setIndent(true);// 设置是否缩进
		outputFormat.setIndent("	");// 以TAB方式实现缩进
		outputFormat.setNewlines(true);// 设置是否换行
		return outputFormat;
	}
	
	/**
	 * 将document写入指定的文件路径中
	 *	@param path 文件路径
	 *	@param document	文档
	 *	@throws Exception
	 *  @date :2011-11-15
	 */
	public static void writeXML(String path,Document document) throws Exception{
		//写数据
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(path), XMLUtil.getOutputFormat());
		xmlWriter.write(document);
		xmlWriter.close();
	}
	
	/**
	 * 获取单个 XML 节的文本信息。 
	 *	@param path xml文件路径
	 *	@param nodeName	 节名称
	 *	@return
	 *	@throws Exception
	 *  @date :2011-11-15
	 */
	public static String getNodeText(String path,String nodeName) throws Exception{
		Document document=XMLUtil.getXMLDocument(path);
		Node node=document.selectSingleNode(nodeName);
		return node.getText();
	}
}
