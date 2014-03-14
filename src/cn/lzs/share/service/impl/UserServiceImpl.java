package cn.lzs.share.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.exception.Exceptions;
import cn.lzs.share.common.util.Finder;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.Md5;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.common.util.ValidateUtil;
import cn.lzs.share.dao.RankDao;
import cn.lzs.share.dao.UserDao;
import cn.lzs.share.dao.text.CommentDao;
import cn.lzs.share.dao.text.DocumentDao;
import cn.lzs.share.dao.text.DownLogDao;
import cn.lzs.share.dao.text.ReportDao;
import cn.lzs.share.domain.Rank;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.domain.text.Report;
import cn.lzs.share.service.UserService;
import cn.lzs.share.web.model.UserModel;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="document.documentDao")
	private DocumentDao docDao;
	@Resource(name="document.commentDao")
	private CommentDao commentDao;
	@Resource(name="document.reportDao")
	private ReportDao reportDao;
	@Resource(name="document.downLogDao")
	private DownLogDao logDao;
	@Resource(name="rankDao")
	private RankDao rankDao;
	
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public User loign(HttpServletRequest request,String name, String password) throws Exception {
		User user=SessionUtil.getUser(request);
		if(user==null){
			System.out.println("从数据库中读取数据！");
			user=userDao.get(name, password);
			if(user==null)
				throw new Exception(Exceptions.USER_LOGIN_ERROR);
			if(user.getState()==Constant.LOCK)
				throw new Exception(Exceptions.USER_LOCK);
			
			user.setLastLoginTime(new Date());
			user.setLoginTimes(user.getLoginTimes()+1);
			user.setIP(FormatUtil.getIp(request));
			
			SessionUtil.put(request, "BASE_PASSWORD", password);
			SessionUtil.put(request, Constant.LOGIN_USER,user);
		}
		return user;
	}
	
	public void personal(UserModel model) throws Exception {
		User user=SessionUtil.getUser(model.getRequest());
		user=userDao.get(user.getId());
		model.setUser(user);
	}

	public void docList(UserModel model) throws Exception {
		User user=SessionUtil.getUser(model.getRequest());
		Finder finder=Finder.create("from Document as d where d.user=:user order by uploadTime desc,ifPass desc");
		finder.setPage(model.getPage());
		finder.setPageSize(model.getPageSize());
		finder.setParam("user", user);
		
		model.setDocuments(docDao.pageFind(finder));
		model.setTotalCount(finder.getTotalSize());
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delDoc(UserModel model) throws Exception {
		User user=SessionUtil.getUser(model.getRequest());
		String[] ids=model.getIds().split("@");
		System.out.println("要删除 "+ids.length+" 个文件 ");
		for(String s:ids){
			Document doc=docDao.get(Integer.valueOf(s));
			delDocDo(doc, user);
		}
	}
	private void delDocDo(Document doc,User user)throws Exception{
		if(user.getId()!=doc.getUser().getId())
			throw new Exception(Exceptions.UPLOAD_ILLEGAL_USER);
		
		user=doc.getUser();
		//将全部的文件删除
		File file=new File(Config.getConfig(ConfigItem.APP_HOME)+doc.getOriginPath());
		if(file.exists()){
			user.changeFileSize(-1*file.length());
			file.delete();
		}
		String name=null;
		for(int i=1;i<=doc.getPageNumber();i++){
			name=Config.getConfig(ConfigItem.APP_HOME)+doc.getSwf()+i+".swf";
			file=new File(name);
			if(file.exists())
				file.delete();
		}
		
		commentDao.executeHsql("delete DocComment where document_id="+doc.getId());
		reportDao.delReport(doc);
		logDao.delDocumentLog(doc);
		docDao.delete(doc);
		
		user.changeNumber(-1);
		user=userDao.save(user);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void uploadHeader(UserModel model, MultipartFile file) throws Exception {
		User user=userDao.get(SessionUtil.getUser(model.getRequest()).getId());
		//头像保存目录
		String root=Config.getFaceRoot();
		
		String ext=FormatUtil.getFileSuffix(file.getOriginalFilename());
		String name=FormatUtil.getRandomName()+"."+ext;
		File targetFile=new File(root,name);
		file.transferTo(targetFile);
		
		if(!ValidateUtil.checkFaceExt(ext)){
			targetFile.delete();
			throw new Exception(Exceptions.FACE_EXT_ERROR);
		}
		if(ValidateUtil.checkFaceSize(targetFile)){
			targetFile.delete();
			throw new Exception(Exceptions.FACE_SIZE_ERROR);
		}
		
		//如果有旧的头像就清空
		if(user.getFace()!=null)
			new File(root+user.getFace()).delete();
		user.setFace(name);
		SessionUtil.put(model.getRequest(), Constant.LOGIN_USER, user);
	}

	public void userInfo(UserModel model) throws Exception {
		User user=userDao.get(model.getId());
		model.setUser(user);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void report(UserModel model) throws Exception {
		Document doc=docDao.checkLock(model.getId());
		User user=SessionUtil.getUser(model.getRequest());
		if(reportDao.getReport(doc, user)!=null){
			throw new Exception(Exceptions.REPORT_EXIST);
		}
		
		doc.setReport(doc.getReport()+1);
		if(doc.getReport()>=Config.getNumberLock()){
			doc.setIfLock(true);
			Log.info(doc.getTitle()+" 被锁定了！");
		}
		
		Report r=new Report();
		r.setDocument(doc);
		r.setTime(new Date());
		r.setUser(user);
		reportDao.save(r);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void changePass(HttpServletRequest request) throws Exception {
		User u=SessionUtil.checkLogin(request);
		
		String oldP=request.getParameter("oldP");
		String newP=request.getParameter("newP");
		
		if(!Md5.Encrypt(oldP).equals(u.getPassword()))
			throw new Exception(Exceptions.OLD_PASSWORD_ERROR);
		
		if(newP.length()<Config.passwordLength())
			throw new Exception(Exceptions.PASSWORD_LENGTH+"密码长度至少是"+Config.passwordLength());
		
		u.setPassword(Md5.Encrypt(newP));
		userDao.update(u);
	}

	public Rank getNextRank(HttpServletRequest request) throws Exception {
		User u=SessionUtil.checkLogin(request);
		Rank r=userDao.get(u.getId()).getRank();
		Finder f=new Finder("from Rank as r where r.integral >"+r.getIntegral()+" order by r.integral");
		List<Rank> ranks=rankDao.find(f);
		if(ranks.size()>0)
			return rankDao.find(f).get(0);
		else
			return null;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void upgrade(HttpServletRequest request) throws Exception {
		User u=SessionUtil.checkLogin(request);
		Rank r=getNextRank(request);
		u=userDao.get(u.getId());
		if(r!=null){
			if(r.getIntegral()>u.getIntegral())
				throw new Exception("积分还没有达到升级要求！");
			userDao.executeSql("update user set rank_id="+r.getId()+" where id="+u.getId());
		}
		else{
			throw new Exception("升级错误！可能你已经到达最高等级");
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void register(HttpServletRequest request,String name,String password,String nick) throws Exception {
		// TODO Auto-generated method stub
		User user = new User();
		user.setPassword(Md5.Encrypt(password));
		user.setNick(nick);
		user.setJoinTime(new Date());
		user.setRank(rankDao.getMinRank());
		user.setIntegral(20);
		userDao.save(user);
	}
}