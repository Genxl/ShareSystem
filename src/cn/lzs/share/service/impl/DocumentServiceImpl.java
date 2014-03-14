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

import cn.lzs.share.cache.Cache;
import cn.lzs.share.common.Constant;
import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.core.ConvertHelper;
import cn.lzs.share.common.core.Converter;
import cn.lzs.share.common.exception.Exceptions;
import cn.lzs.share.common.util.Finder;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.common.util.ValidateUtil;
import cn.lzs.share.dao.UserDao;
import cn.lzs.share.dao.text.CategoryDao;
import cn.lzs.share.dao.text.CommentDao;
import cn.lzs.share.dao.text.DocumentDao;
import cn.lzs.share.dao.text.DownLogDao;
import cn.lzs.share.domain.Rank;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.text.Category;
import cn.lzs.share.domain.text.DocComment;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.domain.text.DownLog;
import cn.lzs.share.service.DocumentService;
import cn.lzs.share.web.model.IndexModel;
import cn.lzs.share.web.model.text.CommentModel;
import cn.lzs.share.web.model.text.DocModel;
import cn.lzs.share.web.model.text.SearchModel;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {

	@Resource(name="document.documentDao")
	private DocumentDao docDao;
	@Resource(name="document.categoryDao")
	private CategoryDao cateDao;
	@Resource(name="document.downLogDao")
	private DownLogDao logDao;
	@Resource(name="document.commentDao")
	private CommentDao commentDao;
	@Resource(name="userDao")
	private UserDao userDao;
	
	public void txtToSwf(File txtFile,File pdfFile,File jpgFile)throws Exception{
		if(Config.getSingleConfig(ConfigItem.TXT_CONVERT_METHOD).equalsIgnoreCase(Converter.JODC)){
			txtFile=Converter.txtToUTF8(txtFile);
			Converter.toPDF(txtFile, pdfFile);
		}else{
			Converter.txtToJPG(txtFile, jpgFile);
			Converter.jpgToPDFByItext(jpgFile, pdfFile);
		}
	}

	public Document uploadDocument(HttpServletRequest request,int id) throws Exception {
		Document doc=docDao.get(id);
		User user=SessionUtil.getUser(request);
		if(user==null)
			throw new Exception(Exceptions.NOT_LOGIN);
		if(!user.getId().equals(doc.getUser().getId())){
			throw new Exception(Exceptions.UPLOAD_ILLEGAL_USER);
		}
		if(doc.isIfPass())
			throw new Exception(Exceptions.NOT_UPLOAD);
		
		return doc;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void uploadDocumentDo(DocModel model,MultipartFile file) throws Exception{
		if(ValidateUtil.checkFile(file.getName(), file.getSize()))
			throw new Exception(Exceptions.FILE_NOT_ALLOW);
		
		Document doc=docDao.get(model.getDoc_id());
		User user=doc.getUser();
		if(!SessionUtil.getUser(model.getRequest()).getId().equals(user.getId()))
			throw new Exception(Exceptions.UPLOAD_ILLEGAL_USER);
		
		if(!enoughSpace(user, file.getSize()))
			throw new Exception(Exceptions.NOT_ENOUGH_SPACE);
		
		//服务器项目目录
		String serverPath=model.getRequest().getSession().getServletContext().getRealPath("");
		//文件保存相对目录
		String path=ConvertHelper.getDocPath(ConvertHelper.TXT)+"/"+SessionUtil.getUser(model.getRequest()).getId();
		String name=FormatUtil.getRandomName()+"."+FormatUtil.getFileSuffix(file.getOriginalFilename());
		
		File targetFile=null;
		//如果是有旧的文件，删除旧文件
		if(doc.isIfUpload()){
			targetFile=new File(serverPath+doc.getOriginPath()); 
			user.changeFileSize(-1*targetFile.length());
			targetFile.delete();
		}else{
			user.changeNumber(1);
		}
		
		targetFile=new File(serverPath+path);
		if(!targetFile.exists()){
			System.out.println(targetFile.getAbsolutePath()+" 这个目录不存在，现在创建！");
			targetFile.mkdir();
		}
		targetFile=new File(serverPath+path,name);
		file.transferTo(targetFile);
		
		doc.setSize(targetFile.length());
		doc.setOriginName(file.getOriginalFilename());
		doc.setOriginPath(path+"/"+name);
		doc.setIfUpload(true);
		doc.initFileType();
		//如果不是可以转换格式的 文件类型，不能在线阅读
		if(!ConvertHelper.canConvert(doc.getFileType()))
			doc.setCanReadOnLine(false);
		
		user.changeFileSize(doc.getSize());
		
		docDao.update(doc);
		userDao.update(user);
		
		model.setDoc(doc);
		System.out.println("保存文件成功："+targetFile.getAbsolutePath());
	}
	
	public boolean enoughSpace(User user,long size){
		Rank rank=user.getRank();
		long total=user.getFileSize()+size;
		return rank.getSize()>=total;
	}
	
	public void addDocument(DocModel model)throws Exception{
		model.setCategorys(cateDao.getParentCategory());
		if(model.getDoc_id()>0){
			Document doc=docDao.get(model.getDoc_id());
			User user=SessionUtil.getUser(model.getRequest());
			if(!user.getId().equals(doc.getUser().getId()))
				throw new Exception(Exceptions.UPLOAD_ILLEGAL_USER+":session_user="+user.getId()+" and doc_user="+doc.getUser().getId());
			model.setDoc(doc);
		}else{
			model.setDoc(new Document());
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void addDocumentDo(DocModel model) throws Exception {
		Document doc=model.getDoc();
		User user=SessionUtil.getUser(model.getRequest());
		doc.setUser(user);
		if(doc.getId()==null||doc.getId()<=0){
			doc.setUploadTime(new Date());
			doc.setCategory(cateDao.get(model.getCategory_id()));
			docDao.save(doc);
		}else{
			Document doc2=docDao.get(doc.getId());
			if(!user.getId().equals(doc.getUser().getId()))
				throw new Exception(Exceptions.UPLOAD_ILLEGAL_USER);
			
			doc2.setTitle(doc.getTitle());
			doc2.setTag(doc.getTag());
			doc2.setCanComment(doc.isCanComment());
			doc2.setCanReadOnLine(doc.isCanReadOnLine());
			doc2.setInformation(doc.getInformation());
			doc2.setDownPrice(doc.getDownPrice());
			docDao.update(doc2);
		}
		
		model.setDoc(doc);
		Log.info("保存文档信息成功！ user:"+doc.getUser().getName()+" 标题："+doc.getTitle());
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Document view(int id) throws Exception {
		Document doc=docDao.checkLock(id);
		
		if(!doc.isIfPass())
			throw new Exception(Exceptions.DOC_NOT_PASS);
		
		doc.setReadNumber(doc.getReadNumber()+1);
		return doc;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Document downLoad(HttpServletRequest request,int id,String name) throws Exception{
		Document doc=docDao.checkLock(id);
		if(!doc.getSaveName().startsWith(name))
			throw new Exception(Exceptions.DOWNLOG_ERROR);
		
		User user=SessionUtil.getUser(request);
		User uploadUser=doc.getUser();
		
		if(!doc.isIfUpload())
			throw new Exception(Exceptions.DOC_NOT_UPLOAD);
		//如果是下载自己的文件，就算没有通过审核也可以下载
		if(!doc.isIfPass()&&user.getId()!=uploadUser.getId())
			throw new Exception(Exceptions.DOC_NOT_PASS);
		//同一个用户时，直接返回
		if(user.getId()==uploadUser.getId()){
			System.out.println("自己下载自己的文件！！！！！！！！！！！！！");
			return doc;
		}
		if(!user.canDownLoad(doc.getDownPrice()))
			throw new Exception(Exceptions.NOT_ENOUGH_MONEY+"<br />所需积分："+doc.getDownPrice()+
					"但是你的积分为："+user.getIntegral());
		
		doc.setDownNumber(doc.getDownNumber()+1);
		processDown(doc, user, uploadUser);
		
		user=(User)userDao.update(user);
		userDao.update(uploadUser);
		docDao.update(doc);
		
		SessionUtil.put(request, Constant.LOGIN_USER, user);
		System.out.println(user.getName()+" 下载一个文件，使用了积分："+doc.getDownPrice());
		
		return doc;
	}
	
	//对下载进行处理，判断是否重复下载，判断是否需要重复扣除积分
	public void processDown(Document doc,User user,User uploadUser)throws Exception{
		DownLog log=logDao.getLog(doc, user);
		boolean cutMoney=true;
		if(log!=null){
			System.out.println("已经有了下载记录！"+log.getId());
			String timeout=Config.getSingleConfig(ConfigItem.DOWNLOAD_TIMEOUT);
			/*
			 * 如果为0 就是重复下载不需要扣除积分
			 */
			if(!timeout.equalsIgnoreCase("0")){
				if(log.isTimeout(timeout)){
				}else{
					//没有超时就不需要扣除积分
					cutMoney=false;
				}
			}
		}else{
			addDownLog(doc, user);
		}
		
		if(cutMoney){
			System.out.println("更新用户积分!!!!!!!!!!!!");
			user.setIntegral(user.getIntegral()-doc.getDownPrice());
			uploadUser.setIntegral(uploadUser.getIntegral()+doc.getDownPrice());
		}
	}
	public void addDownLog(Document doc,User user) throws Exception{
		DownLog log=new DownLog();
		log.setDocument(doc);
		log.setUser(user);
		log.setDownTime(new Date());
		logDao.save(log);
	}

	/**
	 * 首页
	 */
	public void index(IndexModel model) throws Exception {
		//使用Cache
		model.setCategorys(Cache.getCategorys());
		model.setGoodList(Cache.getIndexDocs(Document.GOOD));
		model.setHotList(Cache.getIndexDocs(Document.DWONLOAD));
		model.setNewList(Cache.getIndexDocs(Document.TIME));
		model.setUsers(Cache.getUsers());
		model.setTipsList(Cache.getTips());
	}

	public void category(SearchModel model)throws Exception{
		model.setKey(null);
		
		Category c=cateDao.get(model.getC_id());
		boolean bigC=c.getParentCategory()==null;//是否顶级大类
		
		Finder finder=getSearchFinder(model, bigC, c);
		model.setDocuments(docDao.pageFind(finder));
		
		model.setMaxPage(finder.getMaxPage());
		model.setTotalCount(finder.getTotalSize());
		//如果存在父类，加载的是父类
		if(bigC)
			model.setCategory(c);
		else
			model.setCategory(c.getParentCategory());
	}

	public void search(SearchModel model) throws Exception {
		Category c=null;
		if(model.getC_id()>0)
			c=cateDao.get(model.getC_id());
		boolean bigC=c!=null&&c.getParentCategory()==null;//是否顶级大类
		
		Finder finder=getSearchFinder(model, bigC, c);
		model.setDocuments(docDao.pageFind(finder));
		
		//加亮关键字
		if(model.getKey()!=null&&model.getKey().trim().length()>0){
			List<String> keys=FormatUtil.keysToArray(model.getKey());
			System.out.println("加亮中："+keys.size()+"          "+keys.get(0));
			for(Document doc:model.getDocuments()){
				doc.setTitle(FormatUtil.lightKey(doc.getTitle(), keys));
				//在搜索页面中不需要显示文档信息，这里就不需要加亮了
				//doc.setInformation(FormatUtil.lightKey(doc.getInformation(), keys));
				doc.setTag(FormatUtil.lightKey(doc.getTag(), keys));
			}
		}
		
		model.setMaxPage(finder.getMaxPage());
		model.setTotalCount(finder.getTotalSize());
	}
	
	/**
	 * 获取分类的相关文档，可以是同分类的最新，或者最热
	 */
	public List<Document> getAboutDocsByCategory(String type,int length,int id)throws Exception{
		SearchModel model=new SearchModel();
		
		if(type.equals(Document.DWONLOAD))
			model.setSort("downNumber");
		if(type.equals(Document.TIME))
			model.setSort("uploadTime");
		
		model.setDesc(true);
		model.setPageSize(length);
		
		Category c=cateDao.get(id);
		boolean bigC=c.getParentCategory()==null;//是否顶级大类
		
		Finder finder=getSearchFinder(model, bigC, c);
		System.out.println(finder.getOrigHql());
		
		return docDao.pageFind(finder);
	}
	
	public Finder getSearchFinder(SearchModel model,boolean bigC,Category c)throws Exception{
		if(model.getSort()==null)
			model.setSort("uploadTime");
		
		String sql="from Document as d where d.ifPass=:pass";
		if(bigC){
			sql+=" and (d.category.parentCategory=:c or d.category=:c)";
		}else if(c!=null)
			sql+=" and d.category=:c";
		
		if(model.getDoc_type()!=null&&model.getDoc_type().trim().length()>1){
			sql+=" and d.fileType='"+model.getDoc_type()+"'";
		}
		//如果存在关键字的搜索，需要另作处理
		if(model.getKey()!=null){
			String like=FormatUtil.toSQLLike(model.getKey());
			System.out.println(like+" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			sql+=" and (d.title like '"+like+"' "+
				"or d.tag like '"+like+"') ";
		}
		sql+=" order by "+model.getSort();
		if(model.isDesc())
			sql+=" desc ";
		
		Finder finder=Finder.create(sql);
		if(c!=null)
			finder.setParam("c",c);
		finder.setParam("pass", true);
		finder.setPage(model.getPage());
		finder.setPageSize(model.getPageSize());
		return finder;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void addComment(CommentModel model) throws Exception {
		Document doc=docDao.checkLock(model.getId());
		
		DocComment comment=model.getComment();
		comment.setUser(SessionUtil.getUser(model.getRequest()));
		comment.setDocument(doc);
		comment.setDate(new Date());
		commentDao.save(comment);
		System.out.println("-------------------->评论成功！");
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delComment(CommentModel model) throws Exception {
		DocComment comment=commentDao.get(model.getId());
		commentDao.delete(comment);
	}
	public void listComment(CommentModel model) throws Exception {
		Finder finder=Finder.create("from DocComment as d where document_id=:id");
		finder.setPage(model.getPage());
		finder.setPageSize(model.getPageSize());
		finder.setParam("id", model.getId());
		
		model.setComments(commentDao.pageFind(finder));
		model.setTotalCount(finder.getTotalSize());
	}

	public String categoryZTree() throws Exception {
		return Cache.getCategoryJson();
	}

	public void downLog(DocModel model) throws Exception {
		Finder finder=Finder.create("from DownLog where document_id="+model.getDoc_id());
		finder.setPage(1);
		finder.setPageSize(model.getPageNumber());
		
		model.setDownLogs(logDao.pageFind(finder));
	}
}