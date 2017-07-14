package org.ccframe.subsys.core.dto;

import org.ccframe.subsys.core.domain.entity.MemberAccountLog;

public class MemberAccountLogRowDto extends MemberAccountLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 565041499666817293L;

	private String operationMan;

	public String getOperationMan() {
		return operationMan;
	}

	public void setOperationMan(String operationMan) {
		this.operationMan = operationMan;
	}
}
