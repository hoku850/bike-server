package org.ccframe.client.components;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.InputElement;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;

/**
 * 支持原生maxlength设置的文本输出框.
 * @author JIM
 *
 */
public class CcTextField extends TextField{

	private Integer maxLength;
	
	public void setMinLength(int minlength){
		this.addValidator(new MinLengthValidator(minlength));
	}
	
	public void setMaxLength(int maxLength){
		this.maxLength = maxLength;
	}
	
	@Override
	public void setValue(String value){
		super.setValue(value);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				InputElement ie = getCell().getAppearance().getInputElement(getElement()).cast();
				ie.setMaxLength(maxLength);
			}
		});
	}

	@Override
	public void reset(){
		super.reset();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				InputElement ie = getCell().getAppearance().getInputElement(getElement()).cast();
				ie.setMaxLength(maxLength);
			}
		});
	}
	
}
