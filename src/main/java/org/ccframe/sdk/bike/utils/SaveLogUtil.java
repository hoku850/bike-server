package org.ccframe.sdk.bike.utils;

import java.util.Date;

import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.core.domain.entity.MemberAccountLog;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.MemberAccountLogService;

public class SaveLogUtil {
	public static Integer saveLog(Integer memberAccount, Double preVaule, Double afterVaule, Double changeVaule, String reason){

		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		MemberAccountLog memberAccountLog = new MemberAccountLog();
		memberAccountLog.setUserId(user.getUserId());
		memberAccountLog.setMemberAccountId(memberAccount);
		memberAccountLog.setOperationManId(user.getUserId());
		memberAccountLog.setOrgId(user.getOrgId());
		memberAccountLog.setPrevValue(preVaule);
		memberAccountLog.setReason(reason);
		memberAccountLog.setChangeValue(changeVaule);
		memberAccountLog.setAfterValue(afterVaule);
		memberAccountLog.setSysTime(new Date());
		
		MemberAccountLog memberAccountLog2 = SpringContextHelper.getBean(MemberAccountLogService.class).save(memberAccountLog);
		return memberAccountLog2.getMemberAccountLogId();
	}
}
