package cn.lzs.share.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.common.util.Md5;
import cn.lzs.share.domain.User;

@Service("userDao")
public class UserDao extends BaseDao<User>{
	
	public User get(String name,String password){
		Finder f=Finder.create("from User where name=:name and password=:ps");
		System.out.println(password);
		f.setParam("name", name);
		f.setParam("ps", Md5.Encrypt(password));
		List<User> list=find(f);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<User> list(int length,int type,boolean sort){
		String sql="from User as d ";
		if(type==User.DOC_NUMBER)
			sql+="order by document_number ";
		if(type==User.LIVE)
			sql+="order by loginTimes ";
		if(type==User.MONEY)
			sql+="order by integral ";
		if(sort)
			sql+="desc ";
		Finder finder=Finder.create(sql);
		finder.setPageSize(length);
		return find(finder);
	}
}
