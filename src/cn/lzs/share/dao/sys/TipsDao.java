package cn.lzs.share.dao.sys;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.util.Finder;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.sys.Tips;

@Service("system.tipsDao")
public class TipsDao extends BaseDao<Tips>{
	
	/**
	 * 获取首页显示的图片
	 *	@return
	 *  @date :2013-1-4
	 */
	public List<Tips> getIndexTips(){
		Finder finder=Finder.create("from Tips as t where t.ifDie=0 order by t.sort,t.addTime desc");
		finder.setPageSize(Config.indexPicNumber());
		return this.pageFind(finder);
	}
}