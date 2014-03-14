package cn.lzs.share.domain.text;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;
import cn.lzs.share.domain.User;

@Entity
@Table(name="document_comment")
@SuppressWarnings("serial")
public class DocComment extends BaseEntity{
	private String content;
	private Date date;
	
	private User user;
	private Document document;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="document_id",nullable=false)
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
}