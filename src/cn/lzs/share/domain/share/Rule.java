package cn.lzs.share.domain.share;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="share_rule")
@SuppressWarnings("serial")
public class Rule extends BaseEntity{
	private int userId;//用户id
	private String moduleName;//模块名称
	private int contentId;//模块中内容id，比如在文章模块中，这个就是对应的板块id
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
}
