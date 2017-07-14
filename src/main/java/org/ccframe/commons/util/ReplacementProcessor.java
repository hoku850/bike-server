package org.ccframe.commons.util;

import org.ccframe.commons.util.DbUnitUtils.DBTYPE;
import org.dbunit.database.IDatabaseConnection;

import java.sql.SQLException;

public interface ReplacementProcessor {
	Object replacementSubStrToObject(String substring,  IDatabaseConnection connection, DBTYPE dbType) throws SQLException;
	String getStartDelim();
	String getEndDelim();
}
