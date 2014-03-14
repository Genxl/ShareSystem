package cn.lzs.share.service.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.springframework.stereotype.Service;

import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.util.search.SearchHelper;
import cn.lzs.share.web.model.search.ContentModel;
import cn.lzs.share.web.model.search.TableContext;

@Service("search.searchService")
public class SearchEngineService {
	
	public ContentModel listHtml(String param ,  String type) {  
		ContentModel model = new ContentModel();
		StringBuffer html = new StringBuffer();
		try {  
			NodeFilter filter = new TagNameFilter("body");  
			Parser parser = new Parser();  
			parser.setURL(SearchHelper.SEARCH_URL_BAIDU+param);  
			parser.setEncoding(parser.getEncoding()); 
			NodeList list = parser.extractAllNodesThatMatch(filter);  
			String body = list.toHtml();
			
			Parser content = new Parser();
			content.setInputHTML(body);  
			content.setEncoding(parser.getEncoding());  
			NodeFilter content_filter = new TagNameFilter("table"); 
			NodeList content_list = content.extractAllNodesThatMatch(content_filter); 
			for (int i = 0; i < content_list.size(); i++) {  
				String s = content_list.elementAt(i).toHtml();
				if(s.indexOf("div")!=-1){
					continue;
				}

				if(s.indexOf("相关搜索")!=-1){
					
					html.append("<div id=\"rs\">"+s+"</div>");
					continue;
				}
				html.append("<div class=\"content\">");
				for(Node n :extractHtml(content_list.elementAt(i) , type)){
					
					if(n instanceof LinkTag){
						if(n.toPlainTextString().equals("百度快照")){
							continue;
						}
						html.append("<h3 class=\"t\">"+n.toHtml()+"</h3>");
					}else{
						html.append(n.toHtml());
					}
				}
				
				html.append("<br/></div><br>");
			} 
			
			/**
			 * 获取分页数据
			 */
			Parser page = new Parser();
			page.setInputHTML(body);  
			page.setEncoding(parser.getEncoding());  
			NodeFilter page_filter = new TagNameFilter("p"); 
			NodeList page_list = page.extractAllNodesThatMatch(page_filter); 
			for (int i = 0; i < page_list.size(); i++) {  
				String s = page_list.elementAt(i).toHtml();
				if(s.indexOf("page")==-1){
					continue;
				}
				html.append("<p id=\"page\">"+page_list.elementAt(i).toHtml()+"</div>");
			} 
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		
		model.setContent(html.toString());
		return model;
	}  
	

	private int tableNumber;
	@SuppressWarnings("unused")
	private ArrayList<TableContext> htmlContext = new ArrayList<TableContext>();
	
	/**
	 * 递归钻取正文信息
	 * 
	 * @param nodeP
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Node> extractHtml(Node nodeP , String type)
			throws Exception {
		NodeList nodeList = nodeP.getChildren();
		if ((nodeList == null) || (nodeList.size() == 0)) {
			return null;
		}
		ArrayList tableList = new ArrayList();
		try {
			for (NodeIterator e = nodeList.elements(); e.hasMoreNodes();) {
				Node node = (Node) e.nextNode();
				if (node instanceof LinkTag) {
					tableList.add(node);
				}else if (node instanceof ScriptTag
						|| node instanceof StyleTag
						|| node instanceof SelectTag) {
				} else if (node instanceof TextNode) {
					if (node.getText().length() > 0) {
						tableList.add(node);
					}
				} else {
					List tempList = extractHtml(node , type);
					if ((tempList != null) && (tempList.size() > 0)) {
						Iterator ti = tempList.iterator();
						while (ti.hasNext()) {
							tableList.add(ti.next());
						}
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		if ((tableList != null) && (tableList.size() > 0)) {
				TableContext tc = new TableContext();
				tc.setLinkList(new ArrayList());
				tc.setTextBuffer(new StringBuffer());
				tableNumber++;
				tc.setTableRow(tableNumber);
				Iterator ti = tableList.iterator();
				
				//得到设置的搜索URL
				String baseUrl=Config.getSingleConfig(ConfigItem.SEARCH_BASE_URL);
				
				while(ti.hasNext()){
					Node node = (Node)ti.next();
					if(node instanceof LinkTag){
						LinkTag linkTag = (LinkTag)node;
						if(!"1".equalsIgnoreCase(type)){
							linkTag.setAttribute("href", baseUrl+SearchHelper.encrypt(linkTag.getAttribute("href")));
						}
						tc.getLinkList().add(linkTag);
					}else{
						tc.getTextBuffer().append(node.getText());
					}
				}
				return tableList;
			}
		return null;
	}

	
	public ContentModel view(String url){
		
		ContentModel model = new ContentModel();
		
		try {  
			NodeFilter filter = new TagNameFilter("html");  
			Parser parser = new Parser();  
			parser.setURL(SearchHelper.decrypt(url));  
			parser.setEncoding(parser.getEncoding()); 
			//parser.setEncoding("gb2312");
			NodeList list = parser.extractAllNodesThatMatch(filter);  
			for (int i = 0; i < list.size(); i++) {  
				String s = list.elementAt(i).toHtml();
				model.setContent(s);
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		
		return model;
		
	}

}
