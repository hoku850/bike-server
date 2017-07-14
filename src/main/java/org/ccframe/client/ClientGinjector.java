package org.ccframe.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;


/**
 * @author Jim
 *
 */
@GinModules({ ViewModule.class})
public interface ClientGinjector extends Ginjector {
    MenuConfig getMenuConfig();
}
