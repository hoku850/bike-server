package org.ccframe.subsys.bike.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.ListExcelWriter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.utils.AppConstant;
import org.ccframe.subsys.bike.domain.code.ChargeOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.PaymentTypeCodeEnum;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.repository.ChargeOrderRepository;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.MemberAccountSearchService;
import org.ccframe.subsys.core.service.OrgSearchService;
import org.ccframe.subsys.core.service.UserSearchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChargeOrderService extends BaseService<ChargeOrder,java.lang.Integer, ChargeOrderRepository>{

	@Transactional(readOnly=true)
	public String doExport(Integer orgId) throws IOException {
		//生成一个EXCEL导入文件到TEMP,并且文件名用UUID
    	String filePathString = WebContextHolder.getWarPath() + File.separator + Global.EXCEL_EXPORT_TEMPLATE_DIR + File.separator + Global.EXCEL_EXPORT_CHARGE_ORDER;//"war/exceltemplate/goodsInfListExcel.xls";
        
    	ListExcelWriter writer = new ListExcelWriter(filePathString);   //GWT.getHostPageBaseURL()+     
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        
        // 区分总平台跟运营商的导出数据
        List<ChargeOrder> chargeOrders = null;
		if (Global.PLATFORM_ORG_ID != orgId) {
			chargeOrders = SpringContextHelper.getBean(ChargeOrderSearchService.class).findByKey(ChargeOrder.ORG_ID, orgId);
		} else {
			chargeOrders = getRepository().findAll();
		}
		
		for (ChargeOrder chargeOrder : chargeOrders) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(ChargeOrder.CHARGE_ORDER_ID, chargeOrder.getChargeOrderId());
			data.put(ChargeOrder.CHARGE_ORDER_NUM, chargeOrder.getChargeOrderNum());
			
			User user = SpringContextHelper.getBean(UserSearchService.class).getById(chargeOrder.getUserId());
			if (user != null) data.put(User.LOGIN_ID, user.getLoginId());
			
			MemberAccount memberAccount = SpringContextHelper.getBean(MemberAccountSearchService.class).getById(chargeOrder.getMemberAccountId());
			if (memberAccount != null) {
				user = SpringContextHelper.getBean(UserSearchService.class).getById(memberAccount.getUserId());
				if (user != null) data.put(ChargeOrder.MEMBER_ACCOUNT_ID, user.getUserNm());
			}
			data.put(ChargeOrder.MEMBER_ACCOUNT_LOG_ID, chargeOrder.getMemberAccountLogId());
			
			Org org = SpringContextHelper.getBean(OrgSearchService.class).getById(chargeOrder.getOrgId());
			if (org != null) data.put(Org.ORG_ID, org.getOrgNm());
			
			switch (PaymentTypeCodeEnum.fromCode(chargeOrder.getPaymentTypeCode())) {
				case ALIPAY:
					data.put(ChargeOrder.PAYMENT_TYPE_CODE, "支付宝");
					break;
				case WECHAT:
					data.put(ChargeOrder.PAYMENT_TYPE_CODE, "微信");
					break;
				case UNIONPAY:
					data.put(ChargeOrder.PAYMENT_TYPE_CODE, "中国银联");
					break;
				default:
					data.put(ChargeOrder.PAYMENT_TYPE_CODE, "NULL");
					break;
			}
			
			data.put(ChargeOrder.PAYMENT_TRANSACTIONAL_NUMBER, chargeOrder.getPaymentTransactionalNumber());
			data.put(ChargeOrder.CHARGE_AMMOUNT, chargeOrder.getChargeAmmount());
			
			switch (ChargeOrderStatCodeEnum.fromCode(chargeOrder.getChargeOrderStatCode())) {
				case UNPAID:
					data.put(ChargeOrder.CHARGE_ORDER_STAT_CODE, "待支付");
					break;
				case CHARGE_SUCCESS:
					data.put(ChargeOrder.CHARGE_ORDER_STAT_CODE, "充值成功");
					break;
				case CHARGE_FAILED:
					data.put(ChargeOrder.CHARGE_ORDER_STAT_CODE, "充值失败");
					break;
				case REFUNDING:
					data.put(ChargeOrder.CHARGE_ORDER_STAT_CODE, "退款中");
					break;
				case REFUND_SUCCESS:
					data.put(ChargeOrder.CHARGE_ORDER_STAT_CODE, "退款成功");
					break;
				case REFUND_FAILED:
					data.put(ChargeOrder.CHARGE_ORDER_STAT_CODE, "退款失败");
					break;
				default:
					data.put(ChargeOrder.CHARGE_ORDER_STAT_CODE, "NULL");
					break;
			}

			data.put(ChargeOrder.CREATE_TIME_STR, chargeOrder.getCreateTimeStr());
			data.put(ChargeOrder.CHARGE_FINISH_TIME_STR, chargeOrder.getChargeFinishTimeStr());
			data.put(ChargeOrder.REFUND_FINISH_TIME_STR, chargeOrder.getRefundFinishTimeStr());
			
			dataList.add(data);
		}
		String fileName = UUID.randomUUID() + Global.EXCEL_EXPORT_POSTFIX;
     	String outFileName = WebContextHolder.getWarPath() + File.separator + Global.TEMP_DIR + File.separator + fileName;
        writer.fillToFile(dataList, outFileName);
     	
		return JsonBinder.buildNormalBinder().toJson(Global.TEMP_DIR + "/" + fileName);
	}
	
	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public List<ChargeOrder> getChargeDetail(){
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		List<ChargeOrder> list = SpringContextHelper.getBean(ChargeOrderSearchService.class)
				.findByUserIdAndOrgIdOrderByChargeFinishTimeDesc(user.getUserId(), user.getOrgId());
		 
		if(list != null && list.size()>0){
			for(ChargeOrder chargeOrder : list){
				String code = chargeOrder.getPaymentTypeCode();
				if(code.equals(PaymentTypeCodeEnum.ALIPAY.toCode())){
					chargeOrder.setPaymentTypeCode(AppConstant.ALIPAY_CHARGE);
				} else if(code.equals(PaymentTypeCodeEnum.UNIONPAY.toCode())){
					chargeOrder.setPaymentTypeCode(AppConstant.YINLIAN_CHARGE);
				} else if(code.equals(PaymentTypeCodeEnum.WECHAT.toCode())){
					chargeOrder.setPaymentTypeCode(AppConstant.WECHAT_CHARGE);
				}
			}
		}
		
		return list;
	}
}
