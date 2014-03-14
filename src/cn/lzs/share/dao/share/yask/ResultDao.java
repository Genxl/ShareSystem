package cn.lzs.share.dao.share.yask;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.share.yask.Paper;
import cn.lzs.share.domain.share.yask.Result;

@Service("share.yask.resultDao")
public class ResultDao extends BaseDao<Result>{
	
	/**
	 * 获取指定用户名在result中保存的次数
	 *	@param name
	 *	@return
	 *  @date :2012-4-17
	 */
	public int count(Paper p,String name){
		return getListByPaperAndUser(p, name).size();
	}
	
	public List<Result> getListByPaper(Paper p){
		Finder f=Finder.create("from Result where and paper=:ps");
		f.setParam("ps", p);
		List<Result> list=find(f);
		return list;
	}
	
	public List<Result> getListByPaperAndUser(Paper p,String name){
		Finder f=Finder.create("from Result as p where name=:name and paper=:ps");
		f.setParam("name", name);
		f.setParam("ps", p);
		List<Result> list=find(f);
		return list;
	}
}
