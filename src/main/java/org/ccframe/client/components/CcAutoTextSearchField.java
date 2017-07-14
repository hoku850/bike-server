package org.ccframe.client.components;

/**
 * 输入内容触发自动搜索的组件.
 * 输入格式正确则进行校验
 * 
 * 当进行网络异步搜索时，需要注意以下2点：
 * 1) 在搜索的ajax异步请求要做好防止重复请求的处理，例如设置sending的变量来控制是否在请求中.
 * 2) 在异步请求返回时，一定要拿请求前和当前CcAutoTextSearchField的文本内容比较，如果不一致，则需要再重新执行一次validate以免搜索的结果和输入不同步.
 * 
 * @author JIM
 *
 */
public class CcAutoTextSearchField extends CcTextField{

	public static interface SearchHandler{
		void onSearch(String text);
	}
	
	private SearchHandler searchHandler;
	
	public CcAutoTextSearchField(){
		setAutoValidate(true);
		setValidateOnBlur(false);
	}

	@Override
	protected boolean validateValue(String value) {
		boolean ret = super.validateValue(value);
		if(ret){
			searchHandler.onSearch(value);
		}
		return ret;
	}

	public void setSearchHandler(SearchHandler searchHandler){
		this.searchHandler = searchHandler;
	}
	
}
