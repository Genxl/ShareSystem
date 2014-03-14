package cn.lzs.share.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PageModel {
	private int page=1;
	private int maxPage;
	private int pageSize=20;
	private int totalCount;
	
	//生成分页
	private int offset=1;
	private String queryString = null;
	private String actionName = null;
	private String requestString = null;
	private Map<String, String[]> paramMap = new HashMap<String, String[]>();
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getMaxPage() {
		if(maxPage==0&&totalCount>0){
			maxPage=totalCount/pageSize;
			if (totalCount%pageSize>0) {
				this.maxPage++;
			}
		}
		return maxPage;
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}
	public Map<String, String[]> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String[]> paramMap) {
		this.paramMap = paramMap;
	}
	
	/**
	 * 生成全部的参数字符串
	 *	@return
	 *  @date :2011-12-13
	 */
	public String getQueryString() {
		if (this.queryString == null) {
			System.out.println("现在生成queryString--------------"+paramMap.size());
			StringBuffer sb = new StringBuffer("");
			if (paramMap != null && paramMap.size() > 0) {
				for (Iterator<String> iter = paramMap.keySet().iterator(); iter
						.hasNext();) {
					String key = iter.next();
					String[] values = paramMap.get(key);
					for (int i = 0; i < values.length; i++) {
						if (!"page".equals(key)) {
							try {
								sb.append(key).append("=").append(
										URLEncoder.encode(values[i], "UTF-8"))
										.append("&");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			this.queryString = sb.toString();
		}
		return this.queryString;
	}

	/**
	 * 获取 请求 路径
	 *	@return
	 *  @date :2011-12-13
	 */
	public String getRequestString() {
		if (this.requestString == null) {
			StringBuffer sb = new StringBuffer("");
			String param=getQueryString();
			//action为空，就直接使用空字符串
			String action=getActionName();
			if(action==null)
				action="";
			System.out.println(param+"----->");
			sb.append(action).append("?").append(param);
			this.requestString = sb.toString();
		}
		return this.requestString;
	}
	
	/**
	 * 分页代码
	 *	@return
	 *  @date :2011-12-13
	 */
	public String getPageInfo(){
		if(totalCount==0)
			return "";
		StringBuffer sb=new StringBuffer("");
		
		int start=1;
		int end=getMaxPage();
		if(maxPage>2*offset+1){
			start=page-offset;
			end=page+offset;
			if(start<=0){
				start=1;
				end=start+2*offset;
			}
			if(end>maxPage){
				end=maxPage;
				start=maxPage-2*offset;
			}
		}
		if(start>2*offset)
			sb.append("<a href='"+getRequestString()+"page=1'>首页</a>");
		if(page>offset)
			sb.append("<a href='"+getRequestString()+"page="+(page-1)+"'>上页</a>");
		for(;start<=end;start++){
			if(start!=page)
				sb.append("<a class='page' href='"+getRequestString()+"page="+start+"'>["+start+"]</a>");
			else
				sb.append("<span class='current'>&nbsp;"+start+"&nbsp;</span>");
		}
		if(page<=end&&page<maxPage)
			sb.append("<a href='"+getRequestString()+"page="+(page+1)+"'>下页</a>");
		if(end<maxPage-2*offset)
			sb.append("<a href='"+getRequestString()+"page="+maxPage+"'>尾页</a>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;<span class='info'>"+page+"/"+maxPage+" 页,共"+totalCount+"条记录</span>");
		return sb.toString();
	}
}
