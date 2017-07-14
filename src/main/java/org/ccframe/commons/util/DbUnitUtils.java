package org.ccframe.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.ccframe.commons.util.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.db2.Db2DataTypeFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.TransactionOperation;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DbUnitUtils {

	private static final String REPLACEMENT_START_DELIMITER = "##{";
	private static final String REPLACEMENT_END_DELIMITER = "}##";
	
	private static final String SEQUENCE_START_DELIMITER = "$#{";
	private static final String SEQUENCE_END_DELIMITER = "}#$";

	private DbUnitUtils(){}
	
	public static enum DBTYPE{
		MYSQL, H2, MSSQL, ORACLE, ORACLE10, DB2, HSQLDB, PSQL;

		public static DBTYPE fromCode(String code) {
			try {
				return values()[Integer.parseInt(code)];
			} catch (Exception e) {
				return null;
			}
		}

		public String toCode() {
			return Integer.toString(this.ordinal());
		}
	}

	private static void filterDeleteData(FlatXmlDataSet xmlDataSet){
		
	}
	
	private static void loadDbUnitData(DatabaseOperation dbOperation, DataSource dataSource, String schema, DBTYPE dbType, Map<String, String> replacementToken, String... xmlPaths) throws DatabaseUnitException, SQLException { //NOSONAR
		IDatabaseConnection connection = (StringUtils.isNotBlank(schema) ? new DatabaseDataSourceConnection(dataSource, schema) : new DatabaseDataSourceConnection(dataSource));
		try{
			for (String xmlPath : xmlPaths) {
				InputStream input = DbUnitUtils.class.getResourceAsStream(xmlPath);
				
				FlatXmlDataSet xmlDataSet = new FlatXmlDataSetBuilder().setColumnSensing(false).build(input);
				filterDeleteData(xmlDataSet);
				org.dbunit.dataset.ReplacementDataSet replacementDataSet = new org.dbunit.dataset.ReplacementDataSet(xmlDataSet);
				replacementDataSet.setSubstringDelimiters(REPLACEMENT_START_DELIMITER, REPLACEMENT_END_DELIMITER);
				replacementDataSet.addReplacementObject("[null]", null);
				for(Entry<String, String> replacementEntry :replacementToken.entrySet()){
					replacementDataSet.addReplacementSubstring(replacementEntry.getKey(), replacementEntry.getValue());
				}
//				DBSequenceReplacementDataSet dbSequenceReplacementDataSet = new DBSequenceReplacementDataSet(replacementDataSet, connection, dbType);
//				dbSequenceReplacementDataSet.setSubstringDelimiters(SEQUENCE_START_DELIMITER, SEQUENCE_END_DELIMITER);
				
				List<ReplacementProcessor> replacementProcessorList = new ArrayList<ReplacementProcessor>();
				replacementProcessorList.add(new DBSequenceReplacementProcessor());
				IDataSet executeDataSet = new FilteredDataSet(
					new DatabaseSequenceFilter(connection),
//					dbSequenceReplacementDataSet
					new ReplacementDataSet(replacementDataSet, connection, dbType, replacementProcessorList)
				);
				switch(dbType){
				case MYSQL:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
					break;
				case H2:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
					break;
				case MSSQL:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory());
					break;
				case ORACLE:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
					break;
				case ORACLE10:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Oracle10DataTypeFactory());
					break;
				case DB2:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Db2DataTypeFactory());
					break;
				case HSQLDB:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
					break;
				case PSQL:
					connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
					break;
				default:
					return;
				}
				if(dbOperation.equals(DatabaseOperationEx.DELETE_ALL) || dbOperation.equals(DatabaseOperationEx.DELETE_WHERE)){
					dbOperation.execute(connection, executeDataSet);
				}else{
					new TransactionOperation(dbOperation).execute(connection, executeDataSet);
				}
			}
		}finally{
			connection.close();
		}
	}

	/**
	 * Truncate tables define in xml, then insert data.
	 */
	public static void loadDbUnitData(DataSource dataSource, String schema, DBTYPE dbType, Map<String, String> replacementToken, String... xmlPaths) throws DatabaseUnitException, SQLException {
		loadDbUnitData(DatabaseOperationEx.CLEAN_INSERT, dataSource, schema, dbType, replacementToken, xmlPaths);
	}

	public static void removeDbUnitData(DataSource dataSource, String schema, DBTYPE dbType, Map<String, String> replacementToken, String... xmlPaths) throws DatabaseUnitException, SQLException {
		loadDbUnitData(DatabaseOperationEx.DELETE_ALL, dataSource, schema, dbType, replacementToken, xmlPaths);
	}

	public static void deleteDbUnitData(DataSource dataSource, String schema, DBTYPE dbType, Map<String, String> replacementToken, String... xmlPaths) throws DatabaseUnitException, SQLException {
		loadDbUnitData(DatabaseOperationEx.DELETE_WHERE, dataSource, schema, dbType, replacementToken, xmlPaths);
	}

	/**
	 * Only insert data.
	 */
	public static void appendDbUnitData(DataSource dataSource, String schema, DBTYPE dbType, Map<String, String> replacementToken, String... xmlPaths) throws DatabaseUnitException, SQLException {
		loadDbUnitData(DatabaseOperationEx.INSERT, dataSource, schema, dbType, replacementToken, xmlPaths);
	}
}
