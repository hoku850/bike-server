package org.ccframe.client;

import org.ccframe.client.module.core.view.MainFrame;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class Client implements EntryPoint {
    private final ClientGinjector injector = GWT.create(ClientGinjector.class);

    @Override
    public void onModuleLoad() {
        RootPanel.get().clear();
        RootPanel.get().add(new MainFrame().asWidget());
        injector.getMenuConfig();
        GWT.log("gwt start finish.");
    }
}
