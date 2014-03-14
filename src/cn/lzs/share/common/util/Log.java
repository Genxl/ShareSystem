package cn.lzs.share.common.util;

import java.util.Date;

import org.apache.log4j.Logger;

public class Log {
	public static Logger logger = Logger.getLogger(Logger.class); 
	
	public static void info(String message){
		if(logger.isInfoEnabled()){
			logger.info(message);
		}
	}

	public static void debug(String message){
		if(logger.isDebugEnabled()){
			logger.debug(message);
		}
	}

	public static void trace(String message){
		if(logger.isTraceEnabled()){
			logger.trace(message);
		}
	}
	
	public static void error(String message){
		logger.error(message);
	}
	
	public static void error(String message , Exception exception){
		logger.error(message , exception);
	}
	
	public static void error(Exception exception){
        StringBuffer errorMsgBuffer = new StringBuffer();
    	StackTraceElement[] stacks = exception.getStackTrace();
    	errorMsgBuffer.append(exception.getMessage() +"["+ new Date()+"]" + " detail message : \r\n");
    	for (int i = 0; i < stacks.length; i++) {
    	    errorMsgBuffer.append("	at ").append(stacks[i].getClassName());
    	    errorMsgBuffer.append(".").append(stacks[i].getMethodName());
    	    errorMsgBuffer.append("(").append(stacks[i].getFileName());
    	    errorMsgBuffer.append(":").append(stacks[i].getLineNumber()).append(")\r\n");	
    	}	
    	logger.error(errorMsgBuffer.toString());
	}
	
	public static void error(Object c,Exception exception){
		String name=c.getClass().getName();
		name="error class："+name+"\r\n";
		logger.error(name, exception);
	}
	
	public static void main(String[] args) {
		error(new NullPointerException());
	}
}
