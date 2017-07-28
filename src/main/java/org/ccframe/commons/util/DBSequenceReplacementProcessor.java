package org.ccframe.commons.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.ccframe.commons.util.DbUnitUtils.DBTYPE;
import org.dbunit.database.IDatabaseConnection;

public class DBSequenceReplacementProcessor implements ReplacementProcessor {

	private static final String SEQUENCE_START_DELIMITER = "$#{";
	private static final String SEQUENCE_END_DELIMITER = "}#$";

	@Override
	public Object replacementSubStrToObject(String substring, IDatabaseConnection connection, DBTYPE dbType) throws SQLException{
		switch(dbType){
			case ORACLE:
			case ORACLE10:
				Integer result = null;
				Statement st = connection.getConnection().createStatement(); //NOSONAR
				ResultSet rs = st.executeQuery("SELECT " + substring + ".NEXTVAL FROM dual"); //NOSONAR
				rs.next();
				result = rs.getInt(1);
				rs.close();
				st.close();
				return result;
			default:
				return null;
		}
	}

	@Override
	public String getStartDelim() {
		return SEQUENCE_START_DELIMITER;
	}

	@Override
	public String getEndDelim() {
		return SEQUENCE_END_DELIMITER;
	}

}
