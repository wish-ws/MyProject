package com.um.domain.common;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.um.util.BeanUtil;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 */
@Data
public class PaginationSupportDTO<T> implements Serializable{

	private static final long serialVersionUID = 6233240765810813271L;
	
	private List<T> itemList;
	
	/**
	 * 总记录数
	 */
	private long totalCount;
	
	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 当前页 默认1
	 */
	private int currentPage = 1;

	/**
	 * 页面记录数
	 */
	private int pageSize = 20;


	public PaginationSupportDTO(){
		this(20);
	}

	public PaginationSupportDTO(int pageSize){
		this.pageSize = pageSize;
	}
	

	public void copyProperties(PageInfo pageInfo,Class t){
		this.itemList = BeanUtil.transformList(pageInfo.getList(),t);
		this.currentPage = pageInfo.getPageNum();
		this.pageSize = pageInfo.getPageSize();
		this.totalCount = pageInfo.getTotal();
		this.totalPage = pageInfo.getPages();
	}

	public void copyProperties(PageInfo pageInfo){
		this.itemList = pageInfo.getList();
		this.currentPage = pageInfo.getPageNum();
		this.pageSize = pageInfo.getPageSize();
		this.totalCount = pageInfo.getTotal();
		this.totalPage = pageInfo.getPages();
	}


	public List<T> getItemList() {
		if(null == this.itemList) this.itemList = Collections.EMPTY_LIST;
		return itemList;
	}

	public void setItemList(List<T> itemList) {
		this.itemList = itemList;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		
		if((this.totalCount % this.pageSize) == 0){
			this.totalPage = (new Long(this.totalCount/this.pageSize)).intValue();
			
		}else{
			this.totalPage = (new Long((this.totalCount/this.pageSize) + 1)).intValue();
		}
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if(0 == currentPage){
			this.currentPage = 1;
		}else{
			this.currentPage = currentPage;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if(pageSize == 0){
			this.pageSize = 20;
		}else{
			this.pageSize = pageSize;
		}
	}


}
