package org.ccframe.client.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.Radio;

/**
 * 关联动态数据的Radio选择分组.
 * beanName是后端的service名称，该service必须实现ILabelValueListProvider接口。
 * @author JIM
 * //TODO FIX 联动逻辑参考CcLabelValueCombobox
 *
 */
public class CcLabelValueRadioField extends AdapterField<Integer>{

	private Map<Integer, Radio> valueRadioMap = new HashMap<Integer, Radio>();
	protected Integer value;
	private Integer pendingValue;
	private CssFloatLayoutContainer panel;
	private ToggleGroup toggle = new ToggleGroup();
	private boolean resetEmpty;
	private String extraParam; //获取服务器列表时可以传递额外的参数来进行查询，通常用于级联
	private int defaultValue;
	/**
	 * 判断是否在请求中的状态，如果有则将数据保存到pendingValue
	 */
	private boolean ready;
	private String beanName;
	private Runnable afterAsyncReset;

	public void setAfterAsyncReset(Runnable afterAsyncReset){
		this.afterAsyncReset = afterAsyncReset;
	}

	public CcLabelValueRadioField() {
		super(new CssFloatLayoutContainer());
		panel = (CssFloatLayoutContainer)widget;
		panel.getElement().getStyle().setPaddingTop(4, Unit.PX);
		toggle.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>(){
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				for(Entry<Integer, Radio> valueRadioEntry: valueRadioMap.entrySet()){
					if(valueRadioEntry.getValue().equals(event.getValue())){
						CcLabelValueRadioField.this.value = valueRadioEntry.getKey();
						break;
					}
				}

				ValueChangeEvent.fire(CcLabelValueRadioField.this, getValue());
			}
		});
	}

	public String getExtraParam() {
		return extraParam;
	}

	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}

	@Override
	public Integer getValue() {
		for(Entry<Integer, Radio> entry: valueRadioMap.entrySet()){
			if(entry.getValue().getValue()){
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public void setValue(Integer value) {
		if(!ready){
			pendingValue = value;
		}else{
			if(pendingValue != null){
				value = pendingValue;
				pendingValue = null;
			}
			innerSetValue(value);
		}
	}

	private void innerSetValue(Integer value){
		Integer oldValue = this.value;
		if (value != oldValue && (oldValue == null || !oldValue.equals(value))) {
			this.value = value;
			Radio checkRadio = valueRadioMap.get(value);
			if(checkRadio != null){ //有可能由于Radio变化，导致设置的ID无效情况
				toggle.setValue(checkRadio);
			}else{
				this.value = null;
			}
			ValueChangeEvent.fire(this, value);
		}
	}
	
	public void setBeanName(String beanName){
		this.beanName = beanName;
	}
	
	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public void reset() {
		ready = false;
		this.value = null;
		if(resetEmpty){
			toggle.reset();
		}else{
			setValue(defaultValue); //设置pendingValue
		}
		ClientManager.getSimpleLabelValueClient().getLabelValueList(beanName, extraParam, new RestCallback<List<LabelValue>>(){
			@Override
			public void onSuccess(Method method, List<LabelValue> response) {
				//清理原radio
				toggle.removeAll(valueRadioMap.values());
				for(Radio radio: valueRadioMap.values()){
					radio.removeFromParent();
				}
				valueRadioMap.clear();
				//载入新radio
				for(LabelValue labelValue: response){
					Radio radio = new Radio(){
						@Override
						public void reset() {
							//do nothing
						}
					};
					radio.setBoxLabel(labelValue.getLabel());
					radio.getElement().getStyle().setPaddingTop(2, Unit.PX);
					radio.getElement().getStyle().setPaddingBottom(2, Unit.PX);
					panel.add(radio);
					toggle.add(radio);
					valueRadioMap.put(labelValue.getValue(), radio);
				}
				ready = true;
				setValue(pendingValue);
				if(afterAsyncReset != null){
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							afterAsyncReset.run();							
						}
					});
				}
			}
		});
	}
}
