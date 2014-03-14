package cn.lzs.share.dao.text;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.exception.Exceptions;
import cn.lzs.share.common.util.Finder;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.text.Document;

@Service("document.documentDao")
public class DocumentDao extends BaseDao<Document>{
	
	public List<Document> list(int length,String type,boolean sort,int swithLock){
		String sql="from Document as d where ifPass=1 and ifLock=0 "+
			(swithLock==-1?"":("and ifLock="+swithLock+" "));
		if(type.equals(Document.DWONLOAD))
			sql+="order by downNumber ";
		if(type.equals(Document.GOOD))
			sql+=" and ifGood=1 ";
		if(type.equals(Document.PRICE))
			sql+="order by downPrice ";
		if(type.equals(Document.TIME))
			sql+="order by uploadTime ";
		if(sort)
			sql+="desc ";
		Finder finder=Finder.create(sql);
		finder.setPageSize(length);
		return find(finder);
	}
	
	
	public Document checkLock(int id)throws Exception{
		Document doc=get(id);
		
		if(doc.isIfLock())
			throw new Exception(Exceptions.DOC_LOCK);
		
		return doc;
	}
}