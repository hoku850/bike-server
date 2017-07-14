package org.ccframe.client.module.core.event;

import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.base.BaseObjectEvent;
import org.ccframe.subsys.core.domain.entity.Role;

public class RoleSelectEvent extends BaseObjectEvent<Role> {

	public static final Type TYPE = new Type();

	public RoleSelectEvent(Role role) {
		super(role);
	}

    @Override
	public Type<BaseHandler> getAssociatedType() {
		return TYPE;
	}
}
