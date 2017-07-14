package org.ccframe.commons.util;

public class SessionContextHolder {

    private static ThreadLocal<ISessionContext> sessionContextStore = new ThreadLocal<ISessionContext>();

	public static ISessionContext getSessionContextStore() {
		return sessionContextStore.get();
	}

	public static void setSessionContextStore(ISessionContext sessionContext) {
		sessionContextStore.set(sessionContext);
	}


}
