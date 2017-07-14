package org.ccframe.commons.util;

import org.ccframe.commons.util.DbUnitUtils.DBTYPE;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ReplacementTable implements ITable{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplacementTable.class);

    private final ITable table;
    private IDatabaseConnection connection;
    private DBTYPE dbType;
    private List<ReplacementProcessor> processorList;

    public ReplacementTable(ITable table, IDatabaseConnection connection, DBTYPE dbType, List<ReplacementProcessor> processorList)
    {
        this.table = table;
        this.connection = connection;
        this.dbType = dbType;
        this.processorList = processorList;
    }

    ////////////////////////////////////////////////////////////////////////
    // ITable interface

    public ITableMetaData getTableMetaData()
    {
        return table.getTableMetaData();
    }

    public int getRowCount()
    {
        return table.getRowCount();
    }

    public Object getValue(int row, String column) throws DataSetException
    {
        Object value = table.getValue(row, column);

        if (!(value instanceof String))
        {
            return value;
        }

        String strVal = (String)value;
        for(ReplacementProcessor processor: processorList){
            if (processor.getStartDelim() != null && processor.getEndDelim() != null && value.toString().startsWith(processor.getStartDelim()) && value.toString().endsWith( processor.getEndDelim())){
                try {
    				return processor.replacementSubStrToObject(strVal.substring(processor.getStartDelim().length(), strVal.length() - processor.getEndDelim().length()), connection, dbType);
    			} catch (SQLException e) {
    				LOGGER.error(e.getMessage(), e);
    			}
            }
        }
        return strVal;
    }
    
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer();
    	sb.append(getClass().getName()).append("[");
    	sb.append(", table=").append(table);
    	sb.append("]");
    	return sb.toString();
    }
}
