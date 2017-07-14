package org.ccframe.client.components;

import org.ccframe.client.Global;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

/**
 * 只能输入数字的手机号码组件.
 * 与IntegetField不同，此组件绑定的数据类型是String
 * @author JIM
 *
 */
public class CcMobileField extends CcTextField{

	private int lastKeyCode;

	public CcMobileField(){
		this.addValidator(new RegExValidator(Global.MOBILE_CHECK_REGEX, "手机号码格式错误"));
		this.setMaxLength(11);
		this.addKeyDownHandler(new KeyDownHandler(){

			@Override
			public void onKeyDown(KeyDownEvent event) {
			    lastKeyCode = event.getNativeKeyCode();
			}
		});
		
		this.addKeyPressHandler(new KeyPressHandler(){
			@Override
			public void onKeyPress(KeyPressEvent event) {
			    char key = (char) event.getCharCode();
			    NativeEvent nativeEvent = event.getNativeEvent();
			    if (nativeEvent.<XEvent> cast().isSpecialKey(lastKeyCode) || nativeEvent.getCtrlKey()) {
			      return;
			    }

			    boolean paste = key == 'v' && nativeEvent.<XEvent>cast().getCtrlOrMetaKey();
			    if (!paste && (key < '0' || key > '9')) {
			    	nativeEvent.stopPropagation();
			    	nativeEvent.preventDefault();
			    }
			}
		});
	}

}
