package org.ccframe.client.commons;

import java.util.List;

import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;

/**
 * RestyGwt的请求代理类.
 * 使用方式：
 * 1. new 内部的CallBack
 * 2. 在CallBack里执行请求
 * 3. 请求执行完毕后，在MethodCallback里调用RestyGwtPagingLoader的onLoad方法将返回数据传递给表格
 * @author JIM
 *
 * @param <T> 列表每行的对象.
 */
public class RestyGwtPagingLoader<T> extends PagingLoader<PagingLoadConfig, PagingLoadResult<T>>{

	public RestyGwtPagingLoader(){
		super(null);
	}

	public RestyGwtPagingLoader(DataProxy<PagingLoadConfig, PagingLoadResult<T>> proxy) {
		super(proxy);
	}

	public interface CallBack<R>{
		public void call(int offset, int limit, RestyGwtPagingLoader<R> loader);
	}
	
	private CallBack<T> callBack;
	
	public void setCallBack(CallBack<T> callBack) {
		this.callBack = callBack;
	}

	public void onLoad(List<T> list, int totalLength, int offset){
		fireEvent(new LoadEvent<PagingLoadConfig, PagingLoadResult<T>>(new PagingLoadConfigBean(), new PagingLoadResultBean<T>(list, totalLength, offset)));//for test
	}
	
	@Override
	public void load(int offset, int limit) {
	    setOffset(offset);
	    setLimit(limit);
		callBack.call(offset, limit, this);
	}

	@Override
	protected void loadData(final PagingLoadConfig config) {
		callBack.call(config.getOffset(), config.getLimit(), this);
	}
	
}
