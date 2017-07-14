package org.ccframe.commons.base;

import java.io.Serializable;

public interface IHasSearchBuilder<E extends Serializable,PK extends Serializable> {

	Integer getPriority();

	void buildAllIndex();

}
