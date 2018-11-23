package com.um.util;

import java.util.*;

public class StringUtil {

	/**
	 * 默认分隔符英文逗号 ,
	 */
	public static final String defaultSeparator = ",";

	/**
	 * 验证字符串是否有效 空为无效
	 * 
	 * @param str
	 * @return 有效为ture 无效为false
	 */
	public static boolean isValidString(String str) {
		return null != str && 0 < str.trim().length();
	}

	/**
	 * 将集合中的字符串按照指定分隔符划分返回字符串
	 * 
	 * @param list
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String listToString(List<String> list, String separator) {
		if (!isValidString(separator))
			separator = defaultSeparator;
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			sb.append(str).append(separator);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1); // 去除最后的分隔符
		}
		return sb.toString();
	}
	
	/**
	 * 将集合中的字符串按照指定分隔符划分返回字符串
	 * 
	 * @param list
	 * @return
	 */
	public static String listToString(List<Long> list) {
		StringBuilder sb = new StringBuilder();
		for (Long str : list) {
			sb.append(str).append(defaultSeparator);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1); // 去除最后的分隔符
		}
		return sb.toString();
	}

	/**
	 * 将list转为String
	 * 格式如： 'a','b','c','d'
	 * @param sqlList
	 * @return
	 */
	public static String listToSQLString(Collection<String> sqlList){
		if(null!=sqlList&&sqlList.size()>0){
			StringBuffer idLst = new StringBuffer("'");
	    	for(String obj : sqlList){
	    		idLst.append(obj).append("','"); 
	    	}
	    	if(idLst.length()>0) {
	    		idLst=idLst.deleteCharAt(idLst.length()-1);
	    		idLst=idLst.deleteCharAt(idLst.length()-1);
	    	}
	    	return idLst.toString();
		}
		return null;
	}
	
	/**
	 * 将string转换为List<Long>
	 * @param str
	 * @return
	 */
	public static List<Long> stringToLongList(String str){
    	List<Long> ids = new ArrayList<Long>();
		String[] idsStr = str.split(defaultSeparator);
		for(String idStr:idsStr){
			ids.add(Long.parseLong(idStr));
		}
		return ids;
	}
	
	/**
	 * 将string转换为List<String>
	 * @param str
	 * @return
	 */
	public static List<String> stringToList(String str, String separator){
		if(!isValidString(str))return Collections.EMPTY_LIST;
		if (!isValidString(separator)){
			separator = defaultSeparator;
		}
    	List<String> list = new ArrayList<String>();
		String[] strArray = str.split(separator);
		for(String s:strArray){
			list.add(s);
		}
		return list;
	}
	
	/**
	 * 
	 * @param value
	 */
	 public static String htmlEncode(String value) {
	       if (value == null) {
	           return null;
	       }
	       StringBuffer buffer = new StringBuffer();
	       for (int i = 0; i < value.length(); i++) {
	           char c = value.charAt(i);
	           switch (c) {
	           case '<':
	               buffer.append("&lt;");
	               break;
	           case '>':
	               buffer.append("&gt;");
	               break;
	           case '&':
	               buffer.append("&amp;");
	               break;
	           case '"':
	               buffer.append("&quot;");
	               break;
	           case 10:
	        	   break;
	           case 13:
	               break;
	           default:
	               buffer.append(c);
	           }
	       }
	       value = buffer.toString();
	       return value;
	   }
	 /**
	  * 用于oracle用in语句查询时，将list参数分组
	  * 1组输出格式：1,2,3,4
	  * 大于1组，输出格式 ：1,2) or in (3,4
	  * @param ids
	  * @param count 每组的个数 最大1000
	  * @return
	  */
	 public static String getOracleSQLIn(List<Long> ids, int count,String filed) {
		    count = Math.min(count, 1000);
		    int len = ids.size();
		    int size = len % count;
		    if (size == 0) {
		        size = len / count;
		    } else {
		        size = (len / count) + 1;
		    }
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < size; i++) {
		        int fromIndex = i * count;
		        int toIndex = Math.min(fromIndex + count, len);
		        List<Long> tempList = ids.subList(fromIndex, toIndex);
		        for (Long idStr : tempList) {
					sb.append(idStr).append(",");
				}
				if (sb.length() > 0) {
					sb.setLength(sb.length() - 1); // 去除最后的分隔符
				}
				if (i == 0 && (size-1)>0) {
					sb.append(")");
			     }
				if (i != 0 && i!=(size-1)) {
					sb.append(")");
				}
				if (i == 0 && (size-1)>0) {
			        sb.append(" or "+filed+" in (");
			     }
		        if (i != 0 && i!=(size-1)) {
		        	sb.append(" or "+filed+" in (");
		        }
		    }
		    return sb.toString();
		}


	/**
	 * 用值替代短信模板中的参数
	 *
	 * @param params
	 * @param content
	 * @return
	 */
	private String replaceParametersWithValue(Map<String, String> params, String content) {
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (content.indexOf(entry.getKey()) != -1) {
				content = content.replace(entry.getKey(), entry.getValue());
			}
		}
		return content;
	}


	/**
	 * 替换文本中，图片标签的高宽样式
	 * @return
	 */
	public static String replaceImgStyleWidthHeight(String content){
		String pattern = "(<img [^>]*)( (w|style|h|width|height)=\"[^\"]*\")+";

		//先替换调空格
		content = content.replaceAll("\\s*=\\s*","");
		content = content.replaceAll(pattern,"$1");
		content = content.replaceAll(pattern,"$1");
		content = content.replaceAll(pattern,"$1");
		content = content.replaceAll(pattern,"$1");
		content = content.replaceAll(pattern,"$1");
		return content;
	}
}
