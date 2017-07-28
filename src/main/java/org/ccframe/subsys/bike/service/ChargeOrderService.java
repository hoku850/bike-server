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
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.service.OrgSearchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChargeOrderService extends BaseService<ChargeOrder,java.lang.Integer, ChargeOrderRepository>{

	@Transactional(readOnly=true)
	public String doExport(Integer orgId) throws IOException {
		//生成一个EXCEL导入文件到TEMP,并且文件名用UUID
    	String filePathString = WebContextHolder.getWarPath() + File.separator + Global.EXCEL_EXPORT_TEMPLATE_DIR + File.separator + "chargeOrderListExcel.xls";//"war/exceltemplate/goodsInfListExcel.xls";
        
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
			data.put(ChargeOrder.USER_ID, chargeOrder.getUserId());
			data.put(ChargeOrder.MEMBER_ACCOUNT_ID, chargeOrder.getMemberAccountId());
			data.put(ChargeOrder.MEMBER_ACCOUNT_LOG_ID, chargeOrder.getMemberAccountLogId());
			
			Org org = SpringContextHelper.getBean(OrgSearchService.class).getById(chargeOrder.getOrgId());
			if (org != null) {
				data.put(Org.ORG_ID, org.getOrgNm());
			}
			
			switch (PaymentTypeCodeEnum.fromCode(chargeOrder.getPaymentTypeCode())) {
				case ALIPAY:
					data.put("paymentTypeCodeStr", "支付宝");
					break;
				case WECHAT:
					data.put("paymentTypeCodeStr", "微信");
					break;
				case UNIONPAY:
					data.put("paymentTypeCodeStr", "中国银联");
					break;
				default:
					data.put("paymentTypeCodeStr", "NULL");
					break;
			}
			
			data.put("paymentTransactionalNumber", chargeOrder.getPaymentTransactionalNumber());
			data.put("chargeAmmount", chargeOrder.getChargeAmmount());
			
			switch (ChargeOrderStatCodeEnum.fromCode(chargeOrder.getChargeOrderStatCode())) {
				case UNPAID:
					data.put("chargeOrderStatCodeStr", "待支付");
					break;
				case CHARGE_SUCCESS:
					data.put("chargeOrderStatCodeStr", "充值成功");
					break;
				case CHARGE_FAILED:
					data.put("chargeOrderStatCodeStr", "充值失败");
					break;
				case REFUNDING:
					data.put("chargeOrderStatCodeStr", "退款中");
					break;
				case REFUND_SUCCESS:
					data.put("chargeOrderStatCodeStr", "退款成功");
					break;
				case REFUND_FAILED:
					data.put("chargeOrderStatCodeStr", "退款失败");
					break;
				default:
					data.put("chargeOrderStatCodeStr", "NULL");
					break;
			}

			data.put("createTimeStr", chargeOrder.getCreateTimeStr());
			data.put("chargeFinishTimeStr", chargeOrder.getChargeFinishTimeStr());
			data.put("refundFinishTimeStr", chargeOrder.getRefundFinishTimeStr());
			
			dataList.add(data);
		}
		String fileName =  "temp/" + UUID.randomUUID() + ".xls";
     	String outFileName = WebContextHolder.getWarPath() +"/"+ fileName;
        writer.fillToFile(dataList, outFileName);
     	
		return JsonBinder.buildNormalBinder().toJson(fileName);
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
