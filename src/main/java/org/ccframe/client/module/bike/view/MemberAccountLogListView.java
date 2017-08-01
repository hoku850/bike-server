package org.ccframe.client.module.bike.view;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.base.BasePagingListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.module.bike.event.MemberAccountSelectEvent;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.core.domain.code.AccountTypeCodeEnum;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.MemberAccountLogListReq;
import org.ccframe.subsys.core.dto.MemberAccountLogRowDto;
import org.ccframe.subsys.core.dto.OrgDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

@Singleton
public class MemberAccountLogListView extends BasePagingListView<MemberAccountLogRowDto>{

	interface ArticleInfListUiBinder extends UiBinder<Component, MemberAccountLogListView> {}
	private static ArticleInfListUiBinder uiBinder = GWT.create(ArticleInfListUiBinder.class);

	private Integer memberAccountId;
	private AccountTypeCodeEnum accountTypeCode;
	
	@UiField
	public Label title;
	
	@Override
	protected ModelKeyProvider<MemberAccountLogRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<MemberAccountLogRowDto>(){
			@Override
			public String getKey(MemberAccountLogRowDto item) {
				return item.getMemberAccountLogId().toString();
			}
		};
	}
	
	interface MemberAccountLogProperties extends PropertyAccess<MemberAccountLogRowDto> {
		
		ValueProvider<MemberAccountLogRowDto, Integer> memberAccountLogId();
		ValueProvider<MemberAccountLogRowDto, String> sysTimeStr();
		ValueProvider<MemberAccountLogRowDto, Double> prevValue();
		ValueProvider<MemberAccountLogRowDto, Double> changeValue();
		ValueProvider<MemberAccountLogRowDto, Double> afterValue();
		ValueProvider<MemberAccountLogRowDto, String> reason();
		ValueProvider<MemberAccountLogRowDto, String> operationMan();
	}

	@Override
	protected void initColumnConfig(List<ColumnConfig<MemberAccountLogRowDto, ?>> configList) {
		MemberAccountLogProperties props = GWT.create(MemberAccountLogProperties.class);
		configList.add(new ColumnConfigEx<MemberAccountLogRowDto, String>(props.sysTimeStr(), 130, "交易时间", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<MemberAccountLogRowDto, Double>(props.prevValue(), 70, "交易前", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<MemberAccountLogRowDto, Double>(props.changeValue(), 90, "交易预存款", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<MemberAccountLogRowDto, Double>(props.afterValue(), 70, "交易后", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<MemberAccountLogRowDto, String>(props.reason(), 260, "变更原因", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<MemberAccountLogRowDto, String>(props.operationMan(), 110, "操作员", HasHorizontalAlignment.ALIGN_CENTER, true));
	}
	
	@Override
	protected CallBack<MemberAccountLogRowDto> getRestReq() {
		return new CallBack<MemberAccountLogRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<MemberAccountLogRowDto> loader) {
				if (memberAccountId != null) {
					MemberAccountLogListReq memberAccountLogListReq = new MemberAccountLogListReq();
					memberAccountLogListReq.setMemberAccountId(memberAccountId);
					ClientManager.getMemberAccountLogClient().findList(memberAccountLogListReq, offset, limit, new RestCallback<ClientPage<MemberAccountLogRowDto>>(){
						@Override
						public void onSuccess(Method method, ClientPage<MemberAccountLogRowDto> response) {
							loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
						}
					});
				}
			}
		};
	}
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		EventBusUtil.addHandler(MemberAccountSelectEvent.TYPE, new BaseHandler<MemberAccountSelectEvent>() {

			@Override
			public void action(MemberAccountSelectEvent event) {
				final MemberAccount mAccount = event.getObject();
				if (mAccount != null) {
					memberAccountId = mAccount.getMemberAccountId();
					accountTypeCode = AccountTypeCodeEnum.fromCode(mAccount.getAccountTypeCode());
					
					// 改变列表Header的值
					switch (accountTypeCode) {
						case INTEGRAL:
							columnModel.getColumn(2).setHeader("交易积分");
							view.getHeader().refresh(); //强制更新头部
							break;
						case PRE_DEPOSIT:
							columnModel.getColumn(2).setHeader("交易预存款");
							view.getHeader().refresh(); //强制更新头部
							break;
						case DEPOSIT:
							columnModel.getColumn(2).setHeader("交易押金");
							view.getHeader().refresh(); //强制更新头部
							break;
					}
					loader.load();
					
					// 改变title的值
					ClientManager.getAdminUserClient().getById(mAccount.getUserId(), new RestCallback<User>() {
						@Override
						public void onSuccess(Method method, User response) {
							final String baseTitle = response.getLoginId() + "账户交易日志";
							
							// 运营商登陆
							if (Global.PLATFORM_ORG_ID != MainFrame.adminUser.getOrgId()) {
								title.setText(baseTitle);
							} 
							// 总平台
							else {
								ClientManager.getOrgClient().getById(mAccount.getOrgId(), new RestCallback<OrgDto>() {
									@Override
									public void onSuccess(Method method, OrgDto response) {
										switch (accountTypeCode) {
											case INTEGRAL:
												title.setText(baseTitle + "(" + response.getOrgNm() + "积分)");
												break;
											case PRE_DEPOSIT:
												title.setText(baseTitle + "(" + response.getOrgNm() + "预存款)");
												break;
											case DEPOSIT:
												title.setText(baseTitle + "(" + response.getOrgNm() + "押金)");
												break;
										}
									}
								});
							}
						}
					});
					
					// 表格的值
				}
			}
		});
		return widget;
	}
}