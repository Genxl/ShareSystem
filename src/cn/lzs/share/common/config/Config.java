package cn.lzs.share.common.config;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;

import cn.lzs.share.common.util.Log;

public class Config {
	
	private static final String CONFIG_FILE_NAME="system_config.xml";//配置文件的名称
	private static String CONFIG_PATH=null;//配置文件路径
	private static final String ROOT="/longan/";//配置文件的 ROOT
	
	//配置文件中使用到的命名
	//private static final String[] NAMES={"editor_year","editor_month","editor_day","editor_hour","editor_minute","cache_timeout"};
	
	private static Map<String,String> CONFIG;//配置文件的map
	private static Map<String,String> BACK;//配置文件的备份
	
	/**
	 * 直接从配置文件中获取配置信息
	 *	@param key
	 *	@return
	 *	@throws Exception
	 *  @date :2011-12-1
	 */
	public static String getSingleConfig(String key)throws Exception{
		return XMLUtil.getNodeText(getPath(), ROOT+key);
	}
	
	/**
	 * 获取论坛的配置<br />
	 * 如果没有配置信息，就从配置文件中读取
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getConfig(String key) throws Exception{
		if(CONFIG==null)
			initConfig();
		return CONFIG.get(key);
	}
	
	/**
	 * 设置配置信息，直接覆盖
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void setConfig(String key,String value)throws Exception{
		if(CONFIG==null)
			initConfig();
		CONFIG.put(key, value);
	}
	
	/**
	 * 获取config信息
	 */
	public static void initConfig() throws Exception{
		Document document=XMLUtil.getXMLDocument(getPath());
		CONFIG=new HashMap<String,String>();
		for(String name:ConfigItem.getNames()){
			Node minExpNode=document.selectSingleNode(ROOT+name);
			CONFIG.put(name, minExpNode.getText());
		}
	}
	
	/**
	 * 备份一下Map，如果在更新的过程中出错了可以恢复
	 *	
	 *  @date :2012-1-5
	 */
	public static void back(){
		BACK=new HashMap<String,String>();
		for(String name:ConfigItem.getNames()){
			BACK.put(name, CONFIG.get(name));
		}
		Log.info("备份配置 HashMap 成功！");
	}
	
	/**
	 * 保存配置，更新到文件中
	 */
	public static void updateConfig() throws Exception {
		saveConfig(CONFIG);
	}
	
	private static void saveConfig(Map<String,String> map)throws Exception {
		Document document=XMLUtil.getXMLDocument(getPath());
		for(String name:ConfigItem.getNames()){
			Node node=document.selectSingleNode(ROOT+name);
			node.setText(map.get(name));
		}
		XMLUtil.writeXML(getPath(),document);
	}

	public static void comeBack(){
		for(String name:ConfigItem.getNames()){
			CONFIG.put(name, BACK.get(name));
		}
		Log.info("恢复配置 HashMap 成功！");
	}
	
	/**
	 * 将配置信息还原，这个是在保存信息出错时再执行的
	 *	@throws Exception
	 *  @date :2012-1-5
	 */
	public static void saveBack()throws Exception{
		saveConfig(BACK);
		System.out.println("恢复配置文件成功！");
	}
	
	private static final String getPath() throws Exception{
		if(CONFIG_PATH==null){
			try{
				CONFIG_PATH=Thread.currentThread().getContextClassLoader().getResource("/config/").toURI().getPath() + CONFIG_FILE_NAME;
			}catch(Exception e){
				//window下
				CONFIG_PATH=Thread.currentThread().getContextClassLoader().getResource("\\config\\").toURI().getPath() + CONFIG_FILE_NAME;
			}
		}
		return CONFIG_PATH;
	}
	
	public static String getFaceRoot()throws Exception{
		return Config.getConfig(ConfigItem.APP_HOME)+Config.getConfig(ConfigItem.HEADER_PATH)+"/";
	}
	
	/**
	 * 获取设置的缓存更新时间，默认是30分钟
	 *	@return
	 *  @date :2011-12-29
	 */
	public static long getCacheTime(){
		try{
			String t=getConfig(ConfigItem.CACHE_TIME);
			return Long.valueOf(t)*60*1000;
		}catch(Exception s){
			return 30*60*1000;
		}
	}
	
	public static int getNumberLock(){
		try{
			String t=getConfig(ConfigItem.NUMBER_TO_LOCK);
			return Integer.valueOf(t);
		}catch(Exception s){
			return 5;
		}
	}
	
	public static int passwordLength(){
		try{
			String t=getConfig(ConfigItem.PASSWORD_LENGTH);
			return Integer.valueOf(t);
		}catch(Exception s){
			return 6;
		}
	}
	
	public static int indexPicNumber(){
		try{
			return Integer.valueOf(Config.getConfig(ConfigItem.INDEX_PIC_NUMBER));
		}catch(Exception e){return 4;}
	}
	
	/**测试一下*/
	public static void main(String a[]){
		try{
			System.out.println(Config.getSingleConfig(ConfigItem.APP_NAME));
			Log.info("INFO");
			Log.error("ERROR");
			Log.debug("DEBUG");
		}catch(Exception r){
			Log.error(r);
			Log.info(r.getMessage());
			Log.debug(r.getMessage());
		}
	}
}
