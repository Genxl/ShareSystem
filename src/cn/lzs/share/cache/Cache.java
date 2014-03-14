package cn.lzs.share.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lzs.share.common.util.SpringUtil;
import cn.lzs.share.dao.UserDao;
import cn.lzs.share.dao.sys.TipsDao;
import cn.lzs.share.dao.text.CategoryDao;
import cn.lzs.share.dao.text.DocumentDao;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.sys.Tips;
import cn.lzs.share.domain.text.Category;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.service.DocumentService;

public class Cache {
	public static CacheBean categoryJson;//分类json
	public static Map<String,CacheBean> categoryMap=new HashMap<String,CacheBean>();//同类热门文档，
	public static Map<String,CacheBean> docMap=new HashMap<String,CacheBean>();//首页使用的文档，
	public static CacheBean userList;//活力用户列表
	public static CacheBean categorys;//分类
	public static CacheBean pictures;//首页图片缓存
	
	public static void refresh(){
		if(categoryJson!=null)
			categoryJson.setTimeout(-1);
		if(userList!=null)
			userList.setTimeout(-1);
		if(categorys!=null)
			categorys.setTimeout(-1);
		if(pictures!=null)
			pictures.setTimeout(-1);
		
		for(String s:categoryMap.keySet()){
			categoryMap.get(s).setTimeout(-1);
		}
		for(String s:docMap.keySet()){
			CacheBean c=docMap.get(s);
			System.out.println(c.getName());
			c.setTimeout(-1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Tips> getTips(){
		if(pictures==null||pictures.isNeedUpdate()){
			initPictures();
		}
		return (List<Tips>)pictures.getObject();
	}
	public static void initPictures(){
		TipsDao dao=(TipsDao)SpringUtil.getBean("system.tipsDao");
		pictures=new CacheBean();
		pictures.setObject(dao.getIndexTips());
		pictures.setName("首页切换图片缓存");
		debug(pictures);
	}
	
	/*
	 *============================================================== 
	 *分类JSOn
	 *============================================================== 
	 */
	public static String getCategoryJson(){
		if(categoryJson==null||categoryJson.isNeedUpdate()){
			initCategoryJson();
		}
		return (String)categoryJson.getObject();
	}
	public static void initCategoryJson(){
		CategoryDao dao=(CategoryDao)SpringUtil.getBean("document.categoryDao");
		categoryJson=new CacheBean();
		categoryJson.setObject(dao.zTreeJson());
		categoryJson.setName("分类Json缓存");
		debug(categoryJson);
	}
	
	/*
	 *============================================================== 
	 *分类页面中的同类文档列表
	 *============================================================== 
	 */
	@SuppressWarnings("unchecked")
	public static List<Document> getCategoryHotList(Integer id){
		CacheBean c=categoryMap.get(getName(id, Document.DWONLOAD));
		if(c==null||c.isNeedUpdate()){
			c=initCategoryAboutList(id,Document.DWONLOAD);
			categoryMap.put(getName(id, Document.DWONLOAD), c);
		}
		List<Document> list=(List<Document>)c.getObject();
		return list;
	}
	@SuppressWarnings("unchecked")
	public static List<Document> getCategoryNewList(Integer id){
		CacheBean c=categoryMap.get(getName(id, Document.TIME));
		if(c==null||c.isNeedUpdate()){
			c=initCategoryAboutList(id,Document.TIME);
			categoryMap.put(getName(id, Document.TIME), c);
		}
		List<Document> list=(List<Document>)c.getObject();
		return list;
	}
	public static CacheBean initCategoryAboutList(Integer id,String type){
		DocumentService docService=(DocumentService)SpringUtil.getBean("documentService");
		CacheBean c=new CacheBean();
		c.setName("分类["+id+"]的-"+type+"-文档列表");
		try{
			List<Document> list=docService.getAboutDocsByCategory(type, 8, id);
			bindList(list);
			c.setObject(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		debug(c);
		return c;
	}
	
	/*
	 *============================================================== 
	 *首页中的文档列表
	 *============================================================== 
	 */
	@SuppressWarnings("unchecked")
	public static List<Category> getCategorys(){
		if(categorys==null||categorys.isNeedUpdate()){
			initCategorys();
		}
		return (List<Category>)categorys.getObject();
	}
	@SuppressWarnings("unchecked")
	public static List<User> getUsers(){
		if(userList==null||userList.isNeedUpdate()){
			initUsers();
		}
		return (List<User>)userList.getObject();
	}
	@SuppressWarnings("unchecked")
	public static List<Document> getIndexDocs(String type){
		CacheBean c=docMap.get(type);
		if(c==null||c.isNeedUpdate()){
			c=initIndexDocs(type);
			docMap.remove(type);
			docMap.put(type, c);
		}
		List<Document> list=(List<Document>)c.getObject();
		return list;
	}
	
	public static CacheBean initIndexDocs(String type){
		DocumentDao dao=(DocumentDao)SpringUtil.getBean("document.documentDao");
		CacheBean c=new CacheBean();
		c.setName("首页-"+type+"-文档列表");
		try{
			boolean sort=true;
			if(type.equals(Document.GOOD))
				sort=false;
			List<Document> list=dao.list(8,type, sort,0);
			bindList(list);
			c.setObject(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		debug(c);
		return c; 
	}
	
	public static void initCategorys(){
		CategoryDao dao=(CategoryDao)SpringUtil.getBean("document.categoryDao");
		categorys=new CacheBean();

		List<Category> list=dao.getParentCategory();
		for(Category s:list)
			System.out.println(s);
		categorys.setObject(list);
		
		categorys.setName("首页分类缓存");
		debug(categorys);
	}
	
	public static void initUsers(){
		UserDao dao=(UserDao)SpringUtil.getBean("userDao");
		userList=new CacheBean();

		List<User> list=dao.list(10, User.LIVE, true);
		userList.setObject(list);
		
		userList.setName("首页用户列表缓存");
		debug(userList);
	}
	
	/**
	 * 为了防止在Session关闭后，联级的属性不能注入<br />
	 * 先使得 fetch = FetchType.LAZY 的属性全部注入先<br />
	 */
	public static void bindList(List<Document> list){
		for(Document doc:list){
			System.out.println(doc);
		}
	}
	
	public static String getName(Integer id,String type){
		return type+"_"+id;
	}
	
	public static void debug(CacheBean c){
		System.out.println(c.getName()+"      build success!       "+c.getDate());
	}
}