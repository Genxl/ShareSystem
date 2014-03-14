package cn.lzs.share.dao.share.yask;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.share.yask.YStore;

@Service("share.yask.storeDao")
public class YStoreDao extends BaseDao<YStore>{
	
	public YStore get(User user){
		Finder f=Finder.create("from YStore where user_id="+user.getId());
		List<YStore> list=find(f);
		System.out.println(list.size());
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
