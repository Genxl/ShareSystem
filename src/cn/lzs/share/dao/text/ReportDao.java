package cn.lzs.share.dao.text;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.domain.text.Report;

@Service("document.reportDao")
public class ReportDao extends BaseDao<Report> {
	
	/**
	 * 判断用户是否举报过指定文件
	 *	@param doc
	 *	@param user
	 *	@return
	 *  @date :2012-12-12
	 */
	public Report getReport(Document doc,User user){
		Finder finder=Finder.create("from Report where document_id="+doc.getId()+
				" and user_id="+user.getId());
		finder.setPageSize(1);
		List<Report> list=pageFind(finder);
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	public void delReport(Document doc)throws Exception{
		this.executeHsql("delete Report where document_id="+doc.getId());
	}

}
