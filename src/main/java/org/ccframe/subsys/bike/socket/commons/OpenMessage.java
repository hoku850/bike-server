package org.ccframe.subsys.bike.socket.commons;

import org.ccframe.subsys.bike.domain.entity.MemberUser;

public class OpenMessage {

	private MemberUser memberUser;
	
	private boolean unLocked;

	public OpenMessage(MemberUser memberUser, boolean unLocked){
		this.memberUser = memberUser;
		this.unLocked = unLocked;
	}
	
	public MemberUser getMemberUser() {
		return memberUser;
	}

	public void setMemberUser(MemberUser memberUser) {
		this.memberUser = memberUser;
	}

	public boolean isUnLocked() {
		return unLocked;
	}

	public void setUnLocked(boolean unLocked) {
		this.unLocked = unLocked;
	}

}
