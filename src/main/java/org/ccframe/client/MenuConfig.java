package org.ccframe.client;

import java.util.HashMap;
import java.util.Map;

import org.ccframe.client.module.article.view.ArticleInfMgrView;
import org.ccframe.client.module.article.view.ArticleInfWindowView;
import org.ccframe.client.module.bike.view.AgentAppListView;
import org.ccframe.client.module.bike.view.AgentAppWindowView;
import org.ccframe.client.module.bike.view.AgentListView;
import org.ccframe.client.module.bike.view.AgentWindowView;
import org.ccframe.client.module.bike.view.BikeTypeListView;
import org.ccframe.client.module.bike.view.BikeTypeWindowView;
import org.ccframe.client.module.bike.view.ChargeOrderListView;
import org.ccframe.client.module.bike.view.CyclingOrderListView;
import org.ccframe.client.module.bike.view.CyclingOrderWindowView;
import org.ccframe.client.module.bike.view.MemberAccountMgrView;
import org.ccframe.client.module.bike.view.MemberAccountWindowView;
import org.ccframe.client.module.bike.view.SmartLockGrantWindowView;
import org.ccframe.client.module.bike.view.SmartLockImportWindowView;
import org.ccframe.client.module.bike.view.SmartLockListView;
import org.ccframe.client.module.bike.view.SmartLockWindowView;
import org.ccframe.client.module.bike.view.UserToRepairRecordListView;
import org.ccframe.client.module.bike.view.UserToRepairWindowView;
import org.ccframe.client.module.core.view.AboutSystemWindowView;
import org.ccframe.client.module.core.view.AdminRoleMgrView;
import org.ccframe.client.module.core.view.AdminUserListView;
import org.ccframe.client.module.core.view.AdminUserWindowView;
import org.ccframe.client.module.core.view.CacheInfListView;
import org.ccframe.client.module.core.view.ParamListView;
import org.ccframe.client.module.core.view.PreferenceTextEditWindowView;
import org.ccframe.client.module.core.view.UserImportWindowView;
import org.ccframe.client.module.core.view.UserPasswordWindowView;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class MenuConfig {

	private static Map<ViewResEnum, AsyncProvider<? extends IsWidget>> viewRegistry = new HashMap<ViewResEnum, AsyncProvider<? extends IsWidget>>();

	public static AsyncProvider<? extends IsWidget> getProvider(ViewResEnum key) {
	    return viewRegistry.get(key);
	}
	
    @Inject
    public void setArticleInfMgrAsyncProvider(AsyncProvider<ArticleInfMgrView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ARTICLE_INF_MGR, asyncProvider);
    }

    @Inject
    public void setArticleInfWindowAsyncProvider(AsyncProvider<ArticleInfWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ARTICLE_INF_WINDOW, asyncProvider);
    }
    
    @Inject
    public void setAdminUserListAsyncProvider(AsyncProvider<AdminUserListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ADMIN_USER_LIST, asyncProvider);
    }
    
    @Inject
    public void setUserPasswordWindowAsyncProvider(AsyncProvider<UserPasswordWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.USER_PASSWORD_WINDOW, asyncProvider);
    }
    
    @Inject
    public void setAboutSystemWindowAsyncProvider(AsyncProvider<AboutSystemWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ABOUT_SYSTEM, asyncProvider);
    }
    
    @Inject
    public void setAdminUserWindowAsyncProvider(AsyncProvider<AdminUserWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ADMIN_USER_WINDOW, asyncProvider);
    }
    
    @Inject
    public void setArticleRoleMgrAsyncProvider(AsyncProvider<AdminRoleMgrView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ADMIN_ROLE_MGR, asyncProvider);
    }
    
    @Inject
    public void setUserImportWindowAsyncProvider(AsyncProvider<UserImportWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.USER_IMPORT_WINDOW, asyncProvider);
    }
    
    @Inject
    public void setSysParamListAsyncProvider(AsyncProvider<ParamListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ADMIN_SYS_PARAM, asyncProvider);
    }
 
    @Inject
    public void setCacheInfListAsyncProvider(AsyncProvider<CacheInfListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.ADMIN_CACHE_INF, asyncProvider);
    }
    
    @Inject
    public void setPreferenceTextEditWindowViewAsyncProvider(AsyncProvider<PreferenceTextEditWindowView> asyncProvider){
        viewRegistry.put(ViewResEnum.PREFERENCE_TEXT_EDIT_WINDOW, asyncProvider);
    }
    
	/* 以下是项目模块 */
    
    // 单车类型管理
    @Inject
    public void setBikeTypeListAsyncProvider(AsyncProvider<BikeTypeListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.BIKE_TYPE_LIST, asyncProvider);
    }
    
    @Inject
    public void setBikeTypeWindowAsyncProvider(AsyncProvider<BikeTypeWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.BIKE_TYPE_WINDOW, asyncProvider);
    }
    
    // 骑行订单管理
    @Inject
    public void setCyclingOrderListAsyncProvider(AsyncProvider<CyclingOrderListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.CYCLING_ORDER_LIST, asyncProvider);
    }
    
    @Inject
    public void setCyclingOrderWindowAsyncProvider(AsyncProvider<CyclingOrderWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.CYCLING_ORDER_WINDOW, asyncProvider);
    }
    
    // 充值订单管理
    @Inject
    public void setChargeOrderListAsyncProvider(AsyncProvider<ChargeOrderListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.CHARGE_ORDER_LIST, asyncProvider);
    }
    
    // 账户管理
    @Inject
    public void setMemberAccountMgrAsyncProvider(AsyncProvider<MemberAccountMgrView> asyncProvider){
    	viewRegistry.put(ViewResEnum.MEMBER_ACCOUNT_MGR, asyncProvider);
    } 
   
    @Inject
    public void setMemberAccountWindowAsyncProvider(AsyncProvider<MemberAccountWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.MEMBER_ACCOUNT_WINDOW, asyncProvider);
    }
    
    // 运营商管理
    @Inject
    public void setAgentListAsyncProvider(AsyncProvider<AgentListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.AGENT_LIST_LIST, asyncProvider);
    }
    
    @Inject
    public void setAgentWindowAsyncProvider(AsyncProvider<AgentWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.AGENT_WINDOW, asyncProvider);
    }
    
    @Inject
	public void setSmartLockListAsyncProvider(AsyncProvider<SmartLockListView> asyncProvider) {
		viewRegistry.put(ViewResEnum.SMART_LOCK_LIST, asyncProvider);
	}
	
	@Inject
    public void setSmartLockWindowAsyncProvider(AsyncProvider<SmartLockWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.SMART_LOCK_WINDOW, asyncProvider);
    }
	
	@Inject
    public void setSmartLockImportWindowAsyncProvider(AsyncProvider<SmartLockImportWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.SMART_LOCK_IMPORT_WINDOW, asyncProvider);
    }
	
	@Inject
    public void setSmartLockGrantWindowAsyncProvider(AsyncProvider<SmartLockGrantWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.SMART_LOCK_GRANT_WINDOW, asyncProvider);
    }
	
	@Inject
	public void setAgentAppListAsyncProvider(AsyncProvider<AgentAppListView> asyncProvider) {
		viewRegistry.put(ViewResEnum.AGENT_APP_LIST, asyncProvider);
	}
	
	@Inject
    public void setAgentAppWindowAsyncProvider(AsyncProvider<AgentAppWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.AGENT_APP_WINDOW, asyncProvider);
    }
	
	@Inject
    public void setUserToRepairRecordAsyncProvider(AsyncProvider<UserToRepairRecordListView> asyncProvider){
    	viewRegistry.put(ViewResEnum.USER_TO_REPAIR_RECORD, asyncProvider);
    }
	
	@Inject
    public void setUserToRepairRecordWindowAsyncProvider(AsyncProvider<UserToRepairWindowView> asyncProvider){
    	viewRegistry.put(ViewResEnum.USER_TO_REPAIR_WINDOW, asyncProvider);
    }
}

