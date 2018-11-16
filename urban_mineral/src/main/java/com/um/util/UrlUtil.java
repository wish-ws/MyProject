package com.um.util;


import javax.servlet.http.HttpServletRequest;

/**
 * @class_name: UrlUtil
 * @description: URL工具类
 * @author: ws
 * @date: 2018/9/17
 */
public class UrlUtil {
	
	/**
	 * 是否微信浏览器访问
	 * @param request
	 * @return
	 */
	public static boolean isWeixinBrowserVisit(HttpServletRequest request){
		String ua = UrlUtil.getUserAgent(request);
		if (ua != null && ua.toLowerCase().indexOf("micromessenger") > 0) {
			return true;
		}{
			return false;
		}
	}
    
    /**
     * 获取浏览器类型
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request){
    	String ua = request.getHeader("user-agent");
		if(ua == null){
			ua = request.getHeader("User-Agent");
		}
		if(ua != null){
			ua = ua.toLowerCase();
		}
		return ua;
    }
    
    /**
	 * 获得请求域名
	 * @param request
	 * @return
	 */
	public static String getDomainName(HttpServletRequest request) {
		String domain = "";
		String host = request.getHeader("Host");
		if(host != null && host.length() > 0){
			if(host.indexOf(":") > 0){
				domain = host.substring(0,host.indexOf(":"));
			}else{
				domain = host;
			}
		}
		 
		return domain;
	}
	
	/**
	 * 获得客户端IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
	      String ip = request.getHeader("x-forwarded-for");     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("Proxy-Client-IP");     
	     }     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("WL-Proxy-Client-IP");     
	      }     
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	          ip = request.getRemoteAddr();     
	     }     
	     return ip;     
	}
    
}