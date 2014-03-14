package cn.lzs.share.dao.text;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.domain.text.DownLog;

@Service("document.downLogDao")
public class DownLogDao extends BaseDao<DownLog> {
	
	/**
	 * 判断用户是否下载过指定文件
	 *	@param doc
	 *	@param user
	 *	@return
	 *  @date :2012-12-12
	 */
	public DownLog getLog(Document doc,User user){
		List<DownLog> list=getLogs(doc.getId(), user.getId());
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	public void delDocumentLog(Document doc)throws Exception{
		this.executeHsql("delete DownLog where document_id="+doc.getId());
	}
	
	public List<DownLog> getLogs(int id,int uid){
		Finder finder=Finder.create("from DownLog where document_id="+id+
				(uid==0?"":" and user_id="+uid));
		List<DownLog> list=find(finder);
		return list;
	}
}
