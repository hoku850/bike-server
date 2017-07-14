package org.ccframe.client.module.core.view;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.base.BaseListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.commons.StringUtils;
import org.ccframe.client.components.CcCkTextEditor;
import org.ccframe.client.components.CcTextArea;
import org.ccframe.client.components.CcTextField;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.ParamTypeCodeEnum;
import org.ccframe.subsys.core.domain.entity.Param;
import org.ccframe.subsys.core.dto.ParamRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.info.Info;


@Singleton
public class ParamListView extends BaseListView<ParamRowDto>{

	interface ParamListUiBinder extends UiBinder<Component, ParamListView> {}
	private static ParamListUiBinder uiBinder = GWT.create(ParamListUiBinder.class);

	@UiField
	CardLayoutContainer editCardLayout;

	@UiField
	CcTextArea textArea;
	
	@UiField
	CcCkTextEditor ckTextEditor;
	
	@UiField
	Radio yesRadio;
	
	@UiField
	Radio noRadio;
	
	@UiField
	CssFloatLayoutContainer switchRadioLayout;
	
	@UiField
	CssFloatLayoutContainer dynamicSelectionArea;
	
	@UiField
	CcTextField paramNm;
	
	@UiField
	CcTextField paramInnerCoding;
	
	@UiField
	CcTextArea paramDescr;
	
	private ToggleGroup radioToggle;
	
	@UiHandler("editButton")
	public void editButtonClick(SelectEvent e){
		final ParamRowDto item = grid.getSelectionModel().getSelectedItem();
		final String paramValue;
		switch(ParamTypeCodeEnum.fromCode(item.getParamTypeCode())){
			case TEXT:
				paramValue = textArea.getValue();
				break;
			case HTML:
				paramValue = ckTextEditor.getValue();
				break;
			case SWITCH:
				paramValue = BoolCodeEnum.fromValue(yesRadio.getValue()).toCode();
				break;
			case SINGLE_SELECT:
				paramValue = ((Radio)radioToggle.getValue()).getBoxLabel();
				break;
			case MULTI_SELECT:
				List<String> valueList = new ArrayList<String>();
				for(Widget widget: dynamicSelectionArea){
					if(widget instanceof CheckBox){
						CheckBox checkBox = ((CheckBox)widget);
						if(checkBox.getValue()){
							valueList.add(checkBox.getBoxLabel());
						}
					}
				}
				paramValue = StringUtils.join(valueList, ",");
				break;
			default:
				paramValue = "";
		}
		ClientManager.getParamClient().setParamValue(item.getParamId(), paramValue, new RestCallback<Void>(){

			@Override
			public void onSuccess(Method method, Void response) {
				item.setParamValueStr(paramValue);
				store.update(item);
				Info.display("操作完成", "系统参数更新成功");
			}
			
		});
	}
	
	@Override
	protected ModelKeyProvider<ParamRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<ParamRowDto>(){
			@Override
			public String getKey(ParamRowDto item) {
				return item.getParamId().toString();
			}
		};
	}

	interface ParamRowProperties extends PropertyAccess<ParamRowDto> {
		ValueProvider<ParamRowDto, Integer> getParamId();
		ValueProvider<ParamRowDto, String> getParamNm();
		ValueProvider<ParamRowDto, String> getParamInnerCoding();
		ValueProvider<ParamRowDto, String> getParamDescr();
		ValueProvider<ParamRowDto, String> getParamValueStr();
	}
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<ParamRowDto, ?>> configList) {
		ParamRowProperties props = GWT.create(ParamRowProperties.class);
		configList.add(new ColumnConfigEx<ParamRowDto, Integer>(props.getParamId(), 70, "编号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ParamRowDto, String>(props.getParamNm(), 190, "参数名称", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ParamRowDto, String>(props.getParamInnerCoding(), 200, "参数编码", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ParamRowDto, String>(props.getParamValueStr(), 110, "参数值", HasHorizontalAlignment.ALIGN_LEFT, false));
	}

	@Override
	protected void bindOther() {
		editCardLayout.setActiveWidget(editCardLayout.iterator().next());
		view.setAutoFill(true);
		grid.getSelectionModel().addSelectionHandler(new SelectionHandler<ParamRowDto>(){

			@Override
			public void onSelection(SelectionEvent<ParamRowDto> event) {
				ClientManager.getParamClient().getById(event.getSelectedItem().getParamId(), new RestCallback<Param>(){
					@Override
					public void onSuccess(Method method, Param response) {
						loadEditData(response);
					}
				});
			}
		});
		ToggleGroup yesNoToggle = new ToggleGroup();
		yesNoToggle.add(yesRadio);
		yesNoToggle.add(noRadio);
		radioToggle = new ToggleGroup();
	}

	private void loadEditData(Param param){
		paramNm.setValue(param.getParamNm());
		paramInnerCoding.setValue(param.getParamInnerCoding());
		paramDescr.setValue(param.getParamDescr());
		switch(ParamTypeCodeEnum.fromCode(param.getParamTypeCode())){
			case TEXT:
				textArea.setValue(param.getParamValueStr());
				editCardLayout.setActiveWidget(textArea);
				textArea.setHeight(195); //fix height bug
				break;
			case HTML:
				ckTextEditor.setValue(param.getParamValueStr());
				editCardLayout.setActiveWidget(ckTextEditor);
				break;
			case SWITCH:
				if(StringUtils.isBlank(param.getParamSelectItem())){
					yesRadio.setBoxLabel("是");
					noRadio.setBoxLabel("否");
				}else{
					String[] labelArray = param.getParamSelectItem().split(",");
					yesRadio.setBoxLabel(labelArray[0]);
					noRadio.setBoxLabel(labelArray[1]);
				}
				if( BoolCodeEnum.fromCode(param.getParamValueStr()).boolValue()){
					yesRadio.setValue(true);
				}else{
					noRadio.setValue(true);
				}
				editCardLayout.setActiveWidget(switchRadioLayout);
				break;
			case SINGLE_SELECT:
				dynamicSelectionArea.clear();
				radioToggle.clear();
				String[] radioLabelArray = param.getParamSelectItem().split(",");
				for(String labelText: radioLabelArray){
					Radio radio = new Radio();
					radio.setBoxLabel(labelText);
					dynamicSelectionArea.add(radio);
					radioToggle.add(radio);
					if(param.getParamValueStr().equals(labelText)){
						radio.setValue(true);
					}
				}
				editCardLayout.setActiveWidget(dynamicSelectionArea);
				break;
			case MULTI_SELECT:
				dynamicSelectionArea.clear();
				String[] checkboxLabelArray = param.getParamSelectItem().split(",");
				List<String> valueList = new ArrayList<String>();
				if(!StringUtils.isBlank(param.getParamValueStr())){
					for(String value: param.getParamValueStr().split(",")){
						valueList.add(value);
					}
				}
				for(String labelText: checkboxLabelArray){
					CheckBox check = new CheckBox();
					check.setBoxLabel(labelText);
					dynamicSelectionArea.add(check);
					if(valueList.contains(labelText)){
						check.setValue(true);
					}
				}
				editCardLayout.setActiveWidget(dynamicSelectionArea);
				break;
			default:
		}
	}
	
	@Override
	protected Widget bindUi() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	protected CallBack<ParamRowDto> getRestReq() {
		return new CallBack<ParamRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<ParamRowDto> loader) {
				ClientManager.getParamClient().findParamList(new RestCallback<List<ParamRowDto>>(){
					@Override
					public void onSuccess(Method method, List<ParamRowDto> response) {
						loader.onLoad(response, response.size(), 0);
						grid.getSelectionModel().select(0, false);
					}
				});
			}
		};
	}

}
