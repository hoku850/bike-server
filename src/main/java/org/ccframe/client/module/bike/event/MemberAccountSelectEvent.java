package org.ccframe.client.module.bike.event;

import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.base.BaseObjectEvent;
import org.ccframe.subsys.core.domain.entity.MemberAccount;

@SuppressWarnings("rawtypes")
public class MemberAccountSelectEvent extends BaseObjectEvent<MemberAccount> {

	public static final Type TYPE = new Type();

	public MemberAccountSelectEvent(MemberAccount object) {
		super(object);
	}

    @Override
	public Type<BaseHandler> getAssociatedType() {
		return TYPE;
	}

}
