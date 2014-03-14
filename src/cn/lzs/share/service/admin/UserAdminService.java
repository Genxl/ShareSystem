package cn.lzs.share.service.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.lzs.share.domain.admin.Admin;
import cn.lzs.share.domain.admin.UploadItem;
import cn.lzs.share.web.model.UserModel;
import cn.lzs.share.web.model.admin.RankAdminModel;
import cn.lzs.share.web.model.admin.SysModel;

public interface UserAdminService {

	/*
	 * 编辑用户
	 */
	public void editUser(UserModel model) throws Exception;
	public void editUserDo(UserModel model) throws Exception;
	
	public void listRank(RankAdminModel model) throws Exception;
	
	public void addRank(RankAdminModel model) throws Exception;
	
	public void delRank(RankAdminModel model) throws Exception;
	
	public void editRank(RankAdminModel model) throws Exception;
	
	public void editRankDo(RankAdminModel model) throws Exception;
	
	public void listUser(UserModel model)throws Exception;
	
	public void lockUser(UserModel model)throws Exception;
	
	public void listTips(SysModel model)throws Exception;
	public void addTips(SysModel model)throws Exception;
	public void delTips(SysModel model)throws Exception;
	public void editTips(SysModel model)throws Exception;
	
	public Admin login(HttpServletRequest request,String name,String pass)throws Exception;
	/*
	 * 上传文件
	 */
	public UploadItem upload(MultipartFile file,HttpServletRequest request)throws Exception;
	
	/*
	 * 配置
	 */
	public void saveConfig(HttpServletRequest request)throws Exception;
}