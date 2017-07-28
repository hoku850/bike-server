package org.ccframe.client.commons;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 1.兼容GXT的返回数据格式
 * 2.兼容SDK的开发模式输出页面相关的页数据
 * 3.参考spring-data的分页名称定义，可以直接copyProperties
 * 3.关联分页tagLib组件的输出.
 * 
 * @author Jim
 */
public class ClientPage<E> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8803959390342016899L;

	private List<E> list;

	/**
	 * 总记录数.
	 */
	private int totalLength;
	
	/**
	 * 当前页编号，从0开始
	 */
	private int page;
	
	/**
	 * 每页的记录数
	 */
	private int size;
	
	public ClientPage(){}

	public ClientPage(int totalLength, int page, int size, List<E> list){
		this.totalLength = totalLength;
		this.page = page;
		this.size = size;
		this.list = list;
	}
	

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return 总页数，自然页
	 */
	public int getTotalPages(){
		return totalLength <= 1 ? 1 : ((totalLength - 1) / size + 1);
	}
	
	/**
	 * @return 数据位置
	 */
	public int getOffset(){
		return page * size;
	};

}
