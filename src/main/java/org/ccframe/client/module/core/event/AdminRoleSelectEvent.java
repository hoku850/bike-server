package org.ccframe.client.module.core.event;

import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.base.BaseObjectEvent;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.subsys.core.domain.entity.Role;

@SuppressWarnings("rawtypes")
public class AdminRoleSelectEvent extends BaseObjectEvent<Role> {

	public static final Type TYPE = new Type();

	public AdminRoleSelectEvent(Role object) {
		super(object);
	}

    @Override
	public Type<BaseHandler> getAssociatedType() {
		return TYPE;
	}

}
