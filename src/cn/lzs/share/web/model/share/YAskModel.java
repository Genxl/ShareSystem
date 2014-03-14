package cn.lzs.share.web.model.share;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.PageModel;
import cn.lzs.share.domain.share.yask.Paper;
import cn.lzs.share.domain.share.yask.Result;
import cn.lzs.share.domain.share.yask.Topic;
import cn.lzs.share.domain.share.yask.YStore;

public class YAskModel extends PageModel{
	private HttpServletRequest request;
	
	private int id;
	private String ids;
	private String sort;
	
	private YStore ystore;
	private Paper paper;
	private Topic topic;
	private Result result;
	
	private List<Paper> papers;
	private List<Topic> topics;
	private List<Result> results;
	
	private String data;//用于传递字符串，比如输入一个文本来批量添加题目
	private String info;
	
	private String item[];//保存了用户提交的答卷
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Paper> getPapers() {
		return papers;
	}
	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}
	public List<Topic> getTopics() {
		return topics;
	}
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public YStore getYstore() {
		return ystore;
	}
	public void setYstore(YStore ystore) {
		this.ystore = ystore;
	}
	public Paper getPaper() {
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String[] getItem() {
		return item;
	}
	public void setItem(String[] item) {
		this.item = item;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
}