package cn.lzs.share.domain.share.yask;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="share_yask_topic")
@SuppressWarnings("serial")
public class Topic extends BaseEntity{
	private YStore ystore;
	
	private String name;
	private int type;
	private String answer;
	private String items;
	private Date date;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="store_id",nullable=false)
	public YStore getYstore() {
		return ystore;
	}
	public void setYstore(YStore ystore) {
		this.ystore = ystore;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	/**
	 * 题目选项。
	 * 可以设置多个选项。如果是判断题，这个就可以不填。
	 * 单选题时，格式如下：
	 * 	#中国#日本
	 * 
	 * 则表示有两个选项
	 * 
	 *	@return
	 *  @date :2012-4-11
	 */
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
