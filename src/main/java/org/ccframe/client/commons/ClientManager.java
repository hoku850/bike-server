package org.ccframe.client.commons;

import org.ccframe.client.service.AdminRoleClient;
import org.ccframe.client.service.AdminUserClient;
import org.ccframe.client.service.AgentAppClient;
import org.ccframe.client.service.ArticleInfClient;
import org.ccframe.client.service.BikeTypeClient;
import org.ccframe.client.service.CacheInfClient;
import org.ccframe.client.service.ChargeOrderClient;
import org.ccframe.client.service.CyclingOrderClient;
import org.ccframe.client.service.FileInfClient;
import org.ccframe.client.service.MainFrameClient;
import org.ccframe.client.service.MemberAccountClient;
import org.ccframe.client.service.MemberAccountLogClient;
import org.ccframe.client.service.OrgClient;
import org.ccframe.client.service.ParamClient;
import org.ccframe.client.service.SimpleLabelValueClient;
import org.ccframe.client.service.SmartLockClient;
import org.ccframe.client.service.SmartLockGrantClient;
import org.ccframe.client.service.TreeNodeClient;
import org.ccframe.client.service.UserToRepairRecordClient;

import com.google.gwt.core.client.GWT;

/**
 * REST服务单例.
 * @author JIM
 *
 */
public class ClientManager {

	private static TreeNodeClient treeNodeClient;
	public static TreeNodeClient getTreeNodeClient(){
		if(treeNodeClient == null){
			treeNodeClient = GWT.create(TreeNodeClient.class);
		}
		return treeNodeClient;
	}
	
	private static ArticleInfClient articleInfClient;
	public static ArticleInfClient getArticleInfClient(){
		if(articleInfClient == null){
			articleInfClient = GWT.create(ArticleInfClient.class);
		}
		return articleInfClient;
	}
	
	private static MainFrameClient mainFrameClient;
	public static MainFrameClient getMainFrameClient(){
		if(mainFrameClient == null){
			mainFrameClient = GWT.create(MainFrameClient.class);
		}
		return mainFrameClient;
	}

	private static AdminUserClient adminUserClient;
	public static AdminUserClient getAdminUserClient(){
		if(adminUserClient == null){
			adminUserClient = GWT.create(AdminUserClient.class);
		}
		return adminUserClient;
	}
	
	private static AdminRoleClient adminRoleClient;
	public static AdminRoleClient getAdminRoleClient(){
		if(adminRoleClient == null){
			adminRoleClient = GWT.create(AdminRoleClient.class);
		}
		return adminRoleClient;
	}

	private static FileInfClient fileInfClient;
	public static FileInfClient getFileInfClient() {
		if(fileInfClient == null){
			fileInfClient = GWT.create(FileInfClient.class);
		}
		return fileInfClient;
	}

	private static SimpleLabelValueClient simpleLabelValueClient;
	public static SimpleLabelValueClient getSimpleLabelValueClient() {
	    if(simpleLabelValueClient == null){
	    	simpleLabelValueClient = GWT.create(SimpleLabelValueClient.class);
    	}
        return simpleLabelValueClient;
    }

    private static ParamClient paramClient;
	public static ParamClient getParamClient() {
		if(paramClient == null){
			paramClient = GWT.create(ParamClient.class);
        }
        return paramClient;
	}
	
    private static CacheInfClient cacheInfClient;
	public static CacheInfClient getCacheInfClient() {
		if(cacheInfClient == null){
			cacheInfClient = GWT.create(CacheInfClient.class);
        }
        return cacheInfClient;
	}
	
	private static SmartLockClient smartLockClient;
	public static SmartLockClient getSmartLockClient() {
		if(smartLockClient == null){
			smartLockClient = GWT.create(SmartLockClient.class);
        }
        return smartLockClient;
	}
	
	
	private static BikeTypeClient bikeTypeClient;
	public static BikeTypeClient getBikeTypeClient() {
		if (bikeTypeClient == null) {
			bikeTypeClient = GWT.create(BikeTypeClient.class);
		}
		return bikeTypeClient;
	}
	
	private static CyclingOrderClient cyclingOrderClient;
	public static CyclingOrderClient getCyclingOrderClient() {
		if (cyclingOrderClient == null) {
			cyclingOrderClient = GWT.create(CyclingOrderClient.class);
		}
		return cyclingOrderClient;
	}

	private static AgentAppClient agentAppClient;
	public static AgentAppClient getAgentAppClient() {
		if (agentAppClient == null) {
			agentAppClient = GWT.create(AgentAppClient.class);
		}
		return agentAppClient;
	}

	private static ChargeOrderClient chargeOrderClient;
	public static ChargeOrderClient getChargeOrderClient() {
		if (chargeOrderClient == null) {
			chargeOrderClient = GWT.create(ChargeOrderClient.class);
		}
		return chargeOrderClient;
	}
	
	private static SmartLockGrantClient smartLockGrantClient;
	public static SmartLockGrantClient getSmartLockGrantClient() {
		if (smartLockGrantClient == null) {
			smartLockGrantClient = GWT.create(SmartLockGrantClient.class);
		}
		return smartLockGrantClient;
	}
	
	private static UserToRepairRecordClient userToRepairRecordClient;
	public static UserToRepairRecordClient getUserToRepairRecordClient() {
		if (userToRepairRecordClient == null) {
			userToRepairRecordClient = GWT.create(UserToRepairRecordClient.class);
		}
		return userToRepairRecordClient;
	}
	private static MemberAccountClient memberAccountClient;
	public static MemberAccountClient getMemberAccountClient() {
		if (memberAccountClient == null) {
			memberAccountClient = GWT.create(MemberAccountClient.class);
		}
		return memberAccountClient;
	}
	
	private static MemberAccountLogClient memberAccountLogClient;
	public static MemberAccountLogClient getMemberAccountLogClient() {
		if (memberAccountLogClient == null) {
			memberAccountLogClient = GWT.create(MemberAccountLogClient.class);
		}
		return memberAccountLogClient;
	}
	
	private static OrgClient orgClient;
	public static OrgClient getOrgClient() {
		if (orgClient == null) {
			orgClient = GWT.create(OrgClient.class);
		}
		return orgClient;
	}
	
}
