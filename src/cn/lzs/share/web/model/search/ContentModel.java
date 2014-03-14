package cn.lzs.share.web.model.search;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;

@SuppressWarnings("unchecked")
public class ContentModel {

	/**搜索模式*/
	private int type=1;
	
	private String content;
	private String page;
	private List<List<Node>> data = new ArrayList<List<Node>>();
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
