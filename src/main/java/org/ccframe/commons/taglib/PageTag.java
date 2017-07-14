package org.ccframe.commons.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.commons.ClientPage;

/**
 * 参考 http://plugins.jquery.com/paging/ 设置各种特性
 * 中文介绍 http://www.everycoding.com/coding/236.html（不是最新版）
 * [=首页（支持）
 * ]=尾页（支持）
 * <=前一页（支持）
 * >=后一页（支持）
 * n=前页/后页（支持）
 * c=当前页（支持）
 * p=固定后页（暂不支持）
 * q=固定前页（暂不支持）
 * .=显示页区间（暂不支持）
 * -=补充...字符（暂不支持）
 * *=下拉选择页数（暂不支持）
 * @author JIM
 *
 */
public class PageTag extends TagSupport {
	
	private static final long serialVersionUID = 1218217245851197321L;
	
	private static final Pattern pattern = Pattern.compile("[<>\\[\\]]|[nc]+!?"); //[*<>pq\\[\\]().-]|[nc]+!?
	/**
	 * 当页面有多个PageTag时，用于跟踪分页页号的前缀
	 */
	private String pagePerfix;
	
	private ClientPage page;
	
	private String urlStr;
	
	private List<String> patternGroup = new ArrayList<String>();
	
	private Matcher urlReplaceMatcher; //url替换正则工具

	private static final String[] TEXTS_CN = new String[]{"首页","尾页","上一页","下一页"}; //如果要支持国际化则要跟properties结合
	
	/**
	 * 每个页号的样式
	 */
	private String pageClass;
	/**
	 * 首页/前一页/后一页/尾页 无效的样式
	 */
	private String disableClass;
	/**
	 * 当前页的样式
	 */
	private String currentClass;
	/**
	 * 首页的样式
	 */
	private String firstClass;
	/**
	 * 前一页的样式
	 */
	private String prevClass;
	/**
	 * 后一页的样式
	 */
	private String nextClass;
	/**
	 * 尾页的样式
	 */
	private String lastClass;

	/**
	 * 首页/前一页/后一页/尾页 导航是否采用图标模式，如果是图片则文本输出到title，且导航按钮内容文本为空（采用css背景图片）
	 */
	private boolean iconNavButton;

	public PageTag(){
		super();
		reset();
	}
	
	public void setPagePerfix(String pagePerfix){
		this.pagePerfix = pagePerfix;
	}
	
	public void setFormat(String format){
		Matcher matcher = pattern.matcher(format);
		while(matcher.find()){
			patternGroup.add(matcher.group());
		}
	}
	
	public void setCurrentClass(String currentClass) {
		this.currentClass = currentClass;
	}

	public void setDisableClass(String disableClass){
		this.disableClass = disableClass;
	}
	
	public void setPageClass(String pageClass) {
		this.pageClass = pageClass;
	}

	public void setFirstClass(String firstClass) {
		this.firstClass = firstClass;
	}

	public void setPrevClass(String prevClass) {
		this.prevClass = prevClass;
	}

	public void setNextClass(String nextClass) {
		this.nextClass = nextClass;
	}

	public void setLastClass(String lastClass) {
		this.lastClass = lastClass;
	}

	public void setIconNavButton(boolean iconNavButton) {
		this.iconNavButton = iconNavButton;
	}

	public void setPage(Object page) throws JspException{
		if(page != null && page instanceof ClientPage){
			this.page = (ClientPage)page;
		}
	}


	private void writeAnchorTag(int gotoPage, boolean diabled, String text, boolean isCurrentPage, String iconClass, String titleText){
		if(urlStr == null){ //初始化URL
			HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
			String queryString = request.getQueryString();
			if(StringUtils.isEmpty(queryString)){
				queryString = (pagePerfix + "page=0"); //初始化一个替换值
			}else{
				urlReplaceMatcher = Pattern.compile(pagePerfix + "page=\\d+").matcher(queryString);
				if(!urlReplaceMatcher.find()){
					queryString = (queryString + "&" + pagePerfix + "page=0");
				}
			}
			urlStr = request.getRequestURL() + "?" + queryString;
			urlReplaceMatcher = Pattern.compile(pagePerfix + "page=\\d+").matcher(urlStr);
		}
		List<String> cssList = new ArrayList<String>();
		if(diabled){
			cssList.add(disableClass);
		}
		if(isCurrentPage){
			cssList.add(currentClass);
		}
		if(iconClass != null){
			cssList.add(iconClass);
		}
		StringBuilder sb = new StringBuilder("<a class='").append(StringUtils.join(cssList, " "));
		if(titleText != null){
			sb.append("' title='").append(titleText);
		}
		if(diabled){
			sb.append("' href='javascript:void(0);' onclick='return false'>");
		}else{
			String hrefStr = urlReplaceMatcher.replaceFirst(pagePerfix + "page=" + gotoPage);
			sb.append("' href='").append(hrefStr).append("'>");
		}
		sb.append(text).append("</a>");
		try {
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reset(){
		patternGroup.clear();
		urlReplaceMatcher = null;
		urlStr = null;
		pagePerfix = "";
		pageClass = "page";
		disableClass = "disabled";
		currentClass = "curr";
		firstClass = "first";
		lastClass = "last";
		prevClass = "prev";
		nextClass = "next";
		iconNavButton = false;
	}
	
	@Override
	public int doEndTag() throws JspException {
		int currentPageNum = page.getOffset() == 0 ? 0 : (page.getOffset() / page.getSize());
		try {
			pageContext.getOut().write("<div class='pager'>");
			for(String patternChar: patternGroup){
				switch(patternChar){
					case "[": //首页
						writeAnchorTag(1, currentPageNum == 0, iconNavButton ? "" : TEXTS_CN[0], false, firstClass, iconNavButton ? TEXTS_CN[0] : null);
						break;
					case "]": //尾页
						writeAnchorTag(page.getTotalPages(), currentPageNum == page.getTotalPages() - 1, iconNavButton ?  "" : TEXTS_CN[1], false, lastClass, iconNavButton ? TEXTS_CN[1] : null);
						break;
					case "<":
						writeAnchorTag(currentPageNum , currentPageNum == 0, iconNavButton ? "" : TEXTS_CN[2], false, prevClass, iconNavButton ? TEXTS_CN[2] : null);
						break;
					case ">":
						writeAnchorTag(currentPageNum + 2, currentPageNum == (page.getTotalPages() - 1), iconNavButton ? "" : TEXTS_CN[3], false, nextClass, iconNavButton ? TEXTS_CN[3] : null);
						break;
					default: //n和c构成的群组
						int currentPagePos = patternChar.indexOf("c");
						if(currentPagePos == -1){ //没有c，就当中间的为c
							currentPagePos = patternChar.length() / 2;
						}
						int leftOffset = currentPagePos; //左边距离有几个
						int rightOffset = patternChar.length() - currentPagePos - 1; //右边距离有几个
						if(page.getTotalPages() < patternChar.length()){ //数据不足显示页数的情况
							for(int i = 0; i < page.getTotalPages(); i ++){
								writeAnchorTag(i + 1, false, Integer.toString(i + 1), i == currentPageNum, pageClass, null); //显示的物理页比输出页+1
							}
						}else{
							if(currentPageNum < leftOffset){ //c位置要左移情况
								currentPagePos -= (leftOffset - currentPageNum);
							}else if(currentPageNum > page.getTotalPages() - 1 - rightOffset){ //c位置要右移情况
								currentPagePos += (rightOffset - (page.getTotalPages() - 1 - currentPageNum));
							}

							//输出按钮
							for(int i = 0; i < currentPagePos; i ++ ){ //输出左侧按钮
								int userPageNo = currentPageNum - (currentPagePos - i) + 1; //userPageNo从1开始
								writeAnchorTag(userPageNo, false, Integer.toString(userPageNo), false, pageClass, null); //显示的物理页比输出页+1
							}
							writeAnchorTag(currentPageNum + 1, false, Integer.toString(currentPageNum + 1), true, pageClass, null);
							for(int i = currentPagePos + 1; i < patternChar.length(); i ++){
								int userPageNo = currentPageNum + (i - currentPagePos) + 1;
								writeAnchorTag(userPageNo, false, Integer.toString(userPageNo), false, pageClass, null); //显示的物理页比输出页+1
							}
						}
				}
			}
			pageContext.getOut().write("</div>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			reset();
		}
		return super.doEndTag();
	}

	@Override
    public void release() {
		reset();
        super.release();
    }
}
