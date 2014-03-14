package cn.lzs.share.domain.text;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="document_category")
@SuppressWarnings("serial")
public class Category extends BaseEntity{
	
	private String title;
	private String information;
	private int sort;///排序
	
	private Category parentCategory;
	private Set<Category> childCategory;
	
	@Column(nullable=false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	public Category getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}
	
	@OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.DELETE})
	@OrderBy("id")
	public Set<Category> getChildCategory() {
		return childCategory;
	}
	public void setChildCategory(Set<Category> childCategory) {
		this.childCategory = childCategory;
	}
	
	@Override
	public String toString(){
		String s=" -name:"+getTitle()+" -child:";
		for(Category c:this.getChildCategory())
			s+=c.getTitle()+"|";
		return s;
	}
}
