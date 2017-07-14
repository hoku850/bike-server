package org.ccframe.client.commons;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;


public class ViewUtil{

	public static interface PromptCallback{
		void onOK(String inputText);
	}
	
	public static void messageBox(String title, String text) {
		MessageBox messageBox = new MessageBox(title, text);
		messageBox.setIcon(MessageBox.ICONS.info());
		messageBox.show();
	}

	public static void messageBox(String title, String text, final Runnable callback) {
		MessageBox messageBox = new MessageBox(title, text);
		messageBox.setIcon(MessageBox.ICONS.info());
		messageBox.addDialogHideHandler(new DialogHideHandler(){
			@Override
			public void onDialogHide(DialogHideEvent event) {
				callback.run();
			}
		});
		messageBox.show();
	}

	public static void confirm(String title, String text, final Runnable callback) {
		ConfirmMessageBox confirmMessageBox = new ConfirmMessageBox(title, text);
		confirmMessageBox.addDialogHideHandler(new DialogHideHandler(){
			@Override
			public void onDialogHide(DialogHideEvent event) {
				if(PredefinedButton.YES.equals(event.getHideButton())){
					callback.run();
				}
			}
		});
		confirmMessageBox.show();
	}

	public static void error(String title, String text) {
        AlertMessageBox alertMessageBox = new AlertMessageBox("错误", text);
        alertMessageBox.show();
	}
	
	public static void prompt(String title, String text, final PromptCallback callback){
		prompt(title, text, "", callback);
	}

	public static PromptMessageBox prompt(String title, String text, final String defaultText, final PromptCallback callback){
		final PromptMessageBox promptMessageBox = new PromptMessageBox(title, text);
		promptMessageBox.show();
		promptMessageBox.getField().setValue(defaultText);
		promptMessageBox.addDialogHideHandler(new DialogHideHandler(){
			@Override
			public void onDialogHide(DialogHideEvent event) {
				if(PredefinedButton.OK.equals(event.getHideButton())){
					callback.onOK(promptMessageBox.getValue());
				}
			}
		});
		
		return promptMessageBox;
	}
}
