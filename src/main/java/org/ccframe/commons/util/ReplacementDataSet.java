package org.ccframe.commons.util;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.commons.util.DbUnitUtils.DBTYPE;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplacementDataSet extends AbstractDataSet {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplacementDataSet.class);

    private final IDataSet dataSet;
    private IDatabaseConnection connection;
    private DBTYPE dbType;
    private List<ReplacementProcessor> processorList = new ArrayList<ReplacementProcessor>();

    public ReplacementDataSet(IDataSet dataSet, IDatabaseConnection connection, DBTYPE dbType, List<ReplacementProcessor> processorList)
    {
        this.dataSet = dataSet;
        this.connection = connection;
        this.dbType = dbType;
        this.processorList.addAll(processorList); 
    }

    private ReplacementTable createReplacementTable(ITable table)
    {
        return  new ReplacementTable(table, connection, dbType, processorList);
    }

    ////////////////////////////////////////////////////////////////////////////
    // AbstractDataSet class

    protected ITableIterator createIterator(boolean reversed)
            throws DataSetException
    {
        return new ReplacementIterator(reversed ?
                dataSet.reverseIterator() : dataSet.iterator());
    }

    ////////////////////////////////////////////////////////////////////////////
    // IDataSet interface

    public String[] getTableNames() throws DataSetException
    {
        LOGGER.debug("getTableNames() - start");

        return dataSet.getTableNames();
    }

    public ITableMetaData getTableMetaData(String tableName)
            throws DataSetException
    {
        return dataSet.getTableMetaData(tableName);
    }

    public ITable getTable(String tableName) throws DataSetException
    {
        return createReplacementTable(dataSet.getTable(tableName));
    }

    ////////////////////////////////////////////////////////////////////////////
    // ReplacementIterator class

    private class ReplacementIterator implements ITableIterator
    {

//        private final Logger logger = LoggerFactory.getLogger(ReplacementIterator.class);

        private final ITableIterator iterator;

        public ReplacementIterator(ITableIterator iterator)
        {
            this.iterator = iterator;
        }

        ////////////////////////////////////////////////////////////////////////
        // ITableIterator interface

        public boolean next() throws DataSetException
        {
            return iterator.next();
        }

        public ITableMetaData getTableMetaData() throws DataSetException
        {
            return iterator.getTableMetaData();
        }

        public ITable getTable() throws DataSetException
        {
            return createReplacementTable(iterator.getTable());
        }
    }
}
