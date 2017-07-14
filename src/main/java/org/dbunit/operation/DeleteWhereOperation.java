package org.dbunit.operation;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

public class DeleteWhereOperation extends AbstractBatchOperation {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(DeleteWhereOperation.class);

    public DeleteWhereOperation()
    {
        _reverseRowOrder = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // AbstractBatchOperation class

    protected ITableIterator iterator(IDataSet dataSet) throws DatabaseUnitException
    {
        logger.debug("iterator(dataSet={}) - start", dataSet);
        return dataSet.reverseIterator();
    }
    public OperationData getOperationData(ITableMetaData metaData, BitSet ignoreMapping, IDatabaseConnection connection) throws DataSetException
    {
    	if (logger.isDebugEnabled())
    	{
    		logger.debug("getOperationData(metaData={}, ignoreMapping={}, connection={}) - start",
    				new Object[]{ metaData, ignoreMapping, connection });
    	}

        Column[] whereKeys = metaData.getPrimaryKeys();
        if (whereKeys.length == 0)
        {
        	whereKeys = metaData.getColumns();
        }

        // delete from
        StringBuffer sqlBuffer = new StringBuffer(128);
        sqlBuffer.append("delete from ");
        sqlBuffer.append(getQualifiedName(connection.getSchema(),
                metaData.getTableName(), connection));

        // where
        sqlBuffer.append(" where ");
        for (int i = 0; i < whereKeys.length; i++)
        {
            // escape column name
            String columnName = getQualifiedName(null,
                    whereKeys[i].getColumnName(), connection);
            sqlBuffer.append(columnName);

            sqlBuffer.append(" = ?");
            if (i + 1 < whereKeys.length)
            {
                sqlBuffer.append(" and ");
            }
        }

        return new OperationData(sqlBuffer.toString(), whereKeys);
    }
}
