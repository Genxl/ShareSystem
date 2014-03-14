package cn.lzs.share.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.lzs.share.domain.Rank;
import cn.lzs.share.domain.User;
import cn.lzs.share.web.model.UserModel;

public interface UserService {
	
	public void register(HttpServletRequest request,String name,String password,String nick) throws Exception;
	
	public User loign(HttpServletRequest request,String name,String password)throws Exception;
	
	public void personal(UserModel model)throws Exception;
	
	public void docList(UserModel model)throws Exception;
	
	public void delDoc(UserModel model)throws Exception;
	
	public void changePass(HttpServletRequest request)throws Exception;
	
	public void uploadHeader(UserModel model,MultipartFile file)throws Exception;
	
	public void userInfo(UserModel model)throws Exception;
	
	public void report(UserModel model)throws Exception;
	
	/**
	 * 获取用户的下一个等级
	 *	@return
	 *  @date :2012-3-31
	 */
	public Rank getNextRank(HttpServletRequest request)throws Exception;
	public void upgrade(HttpServletRequest request) throws Exception;
	
}