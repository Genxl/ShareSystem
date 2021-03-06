package cn.lzs.share.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.domain.Rank;

@Service("rankDao")
public class RankDao extends BaseDao<Rank>{
	
	/**
	 * 获取官阶最小的那个，判断积分
	 *	@return
	 *  @date :2011-12-26
	 */
	public Rank getMinRank(){
		Finder finder=Finder.create("from Rank order by integral asc");
		List<Rank> list=this.find(finder);
		if(list.size()>0)
			return list.get(0);
		return null;
	}
}