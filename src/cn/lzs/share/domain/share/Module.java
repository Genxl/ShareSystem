package cn.lzs.share.domain.share;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="share_module")
@SuppressWarnings("serial")
public class Module extends BaseEntity{
	private String name;
	private String beanName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}