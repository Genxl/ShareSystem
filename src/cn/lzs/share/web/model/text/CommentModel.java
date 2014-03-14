package cn.lzs.share.web.model.text;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.PageModel;
import cn.lzs.share.domain.text.DocComment;

public class CommentModel extends PageModel{
	private HttpServletRequest request;
	
	private int id;
	private DocComment comment;
	private List<DocComment> comments;
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DocComment getComment() {
		return comment;
	}
	public void setComment(DocComment comment) {
		this.comment = comment;
	}
	public List<DocComment> getComments() {
		return comments;
	}
	public void setComments(List<DocComment> comments) {
		this.comments = comments;
	}
}