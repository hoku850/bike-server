package org.ccframe.commons.util;

import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.DeleteWhereOperation;

public abstract class DatabaseOperationEx extends DatabaseOperation{
    public static final DatabaseOperation DELETE_WHERE = new DeleteWhereOperation();
}
