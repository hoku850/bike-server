package org.ccframe.commons.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.tools.ant.Project;
import org.dbunit.DatabaseUnitException;
import org.dbunit.ant.Export;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.csv.CsvDataSetWriter;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jim Wu
 *
 */
public class MyDBUnitExport extends Export{

    private static final Logger logger = LoggerFactory.getLogger(MyDBUnitExport.class);
    private FlatXmlWriterEx datasetWriter;
    
    public MyDBUnitExport(){
    	this.setOrdered(true);
    }

	@Override
	protected IDataSet getExportDataSet(IDatabaseConnection connection)
			throws DatabaseUnitException, SQLException {
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(super.getExportDataSet(connection));
		replacementDataSet.addReplacementObject(null, "[null]");
		return replacementDataSet;
	}

	@Override
    public void execute(IDatabaseConnection connection) throws DatabaseUnitException{
        logger.debug("execute(connection={}) - start", connection);

        try
        {
            if (getDest() == null)
            {
                throw new DatabaseUnitException("'getDest()' is a required attribute of the <export> step.");
            }

            IDataSet dataset = getExportDataSet(connection);
            logger.debug("dataset tables: " + Arrays.asList(dataset.getTableNames()), Project.MSG_VERBOSE);

			
            // Write the dataset
            if (getFormat().equals(FORMAT_CSV))
            {
                CsvDataSetWriter.write(dataset, getDest());
            }
            else
            {
                OutputStream out = new FileOutputStream(getDest());
                try
                {
                    if (getFormat().equalsIgnoreCase(FORMAT_FLAT))
                    {
                    	writeXML(dataset, out, getEncoding(), getDoctype());
                    }
                    else if (getFormat().equalsIgnoreCase(FORMAT_XML))
                    {
                        XmlDataSet.write(dataset, out, getEncoding());
                    }
                    else if (getFormat().equalsIgnoreCase(FORMAT_DTD))
                    {
                        //TODO Should DTD also support encoding? It is basically an XML file...
                        FlatDtdDataSet.write(dataset, out);//, getEncoding());
                    }
                    else if (getFormat().equalsIgnoreCase(FORMAT_XLS))
                    {
                        XlsDataSet.write(dataset, out);
                    }
                    else
                    {
                        throw new IllegalArgumentException("The given format '"+getFormat()+"' is not supported.");
                    }
                    
                }
                finally
                {
                    out.close();
                }
            }
            
            logger.debug("Successfully wrote file '" + getDest() + "'", Project.MSG_INFO);
            
        }
        catch (SQLException e)
        {
        	throw new DatabaseUnitException(e);
        }
        catch (IOException e)
        {
            throw new DatabaseUnitException(e);
        }
	}

	private void writeXML(IDataSet dataset, OutputStream out, String encoding, String docType) throws UnsupportedEncodingException, DataSetException {
		synchronized(this){
			datasetWriter = new FlatXmlWriterEx(out, encoding);
			datasetWriter.setDocType(docType);
	        datasetWriter.write(dataset);
	        datasetWriter = null;
		}
	}
	
	public int getTotalTableCount() {
		return datasetWriter == null ? 0: datasetWriter.getTotalTableCount();
	}

	public int getProcessedTableCount() {
		return datasetWriter == null ? 0: datasetWriter.getProcessedTableCount();
	}

	public String getProcessingTableName() {
		return datasetWriter == null ? null: datasetWriter.getProcessingTableName();
	}

	public int getCurrentTableTotalCount() {
		return datasetWriter == null ? 0: datasetWriter.getCurrentTableTotalCount();
	}
	
}
