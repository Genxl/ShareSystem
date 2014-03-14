package cn.lzs.share.dao.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.common.util.Md5;
import cn.lzs.share.dao.BaseDao;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.admin.Admin;

@Service("admin.adminDao")
public class AdminDao extends BaseDao<Admin>{
	
	public Admin get(String name,String password){
		Finder f=Finder.create("from Admin where name=:name and password=:ps");
		f.setParam("name", name);
		f.setParam("ps", Md5.Encrypt(password));
		List<Admin> list=find(f);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<Admin> list(int length,int type,boolean sort){
		String sql="from Admin as d ";
		if(type==User.LIVE)
			sql+="order by loginTimes ";
		if(sort)
			sql+="desc ";
		Finder finder=Finder.create(sql);
		finder.setPageSize(length);
		return find(finder);
	}
}
