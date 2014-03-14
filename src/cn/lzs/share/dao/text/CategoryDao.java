package cn.lzs.share.dao.text;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.text.Category;

@Service("document.categoryDao")
public class CategoryDao extends BaseDao<Category>{
	
	/**
	 * 获取全部的基本分类
	 *	@return
	 *  @date :2012-12-10
	 */
	public List<Category> getParentCategory(){
		Finder f=Finder.create("from Category as c where c.parentCategory is null order by c.sort,c.id");
		return find(f);
	}
	
	/**
	 * 返回给zTree的数据， 那么，这里，就选择了自己实现JSON的数据返回<br />
	 * 格式如下：<br />
	 * [{"id":1,"name":"数码产品","isParent":true,"pid":null},{"id":3,"name":"教材书籍","isParent":true,"pid":null},
	 * {"id":4,"name":"日常用品","isParent":true,"pid":null},{"id":6,"name":"服装饰品","isParent":true,"pid":null},
	 * {"id":7,"name":"其他商品","isParent":true,"pid":null},{"id":8,"name":"手机","isParent":false,"pid":1},
	 * {"id":9,"name":"笔记本电脑","isParent":false,"pid":1},{"id":10,"name":"台式机","isParent":false,"pid":1},
	 * {"id":11,"name":"相机","isParent":false,"pid":1},{"id":12,"name":"电脑配件","isParent":false,"pid":1},
	 * {"id":13,"name":"其他","isParent":false,"pid":1},{"id":15,"name":"计算机类","isParent":false,"pid":3},
	 * {"id":16,"name":"艺术类","isParent":false,"pid":3},{"id":17,"name":"文学类","isParent":false,"pid":3}]
	 */
	public String zTreeJson(){
		List<Category> list=getParentCategory();
		
		StringBuffer sb=new StringBuffer("[");
		for(Category c:list){
			sb.append("{'id':"+c.getId()+",'name':'"+c.getTitle()+"','isParent':true,'pid':null},");
			for(Category co:c.getChildCategory())
				sb.append("{'id':"+co.getId()+",'name':'"+co.getTitle()+"','isParent':false,'pid':"+c.getId()+"},");
		}
		
		return sb.substring(0, sb.length()-1)+"]";
	}
}