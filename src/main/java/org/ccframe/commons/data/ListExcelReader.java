package org.ccframe.commons.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ccframe.client.Global;
import org.ccframe.commons.util.WebContextHolder;

/**
 * 循环列表EXCEL读取.
 * 通过xpath来确定对象数据.反射生成对象数据
 * 
 * 注意：为了保证数据录入的正确，请预先设置好各列的类型.
 * 
 * 该
 * 
 * @author Jim
 */
public class ListExcelReader<T> {
	
	private static Pattern pattern = Pattern.compile("\\{\\=(.+?)\\}");
//	private static DecimalFormat decimalFormat = new DecimalFormat("#.00####");

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Class<T> rowClass;
	private List<String> dataTemplate = new ArrayList<String>(); 

	private int batchSize = 1; //一次处理500条
	
	protected ListExcelReader(){}
	public ListExcelReader(String templeteFilePath, Class<T> rowClass){
		this.rowClass = rowClass;
		FileInputStream fileInputStream = null;
		Workbook templateBook = null;
		try{
			fileInputStream = new FileInputStream(templeteFilePath);
			templateBook = new HSSFWorkbook(new POIFSFileSystem(fileInputStream));
			Sheet templateSheet = templateBook.getSheetAt(0);
			if (templateSheet.getLastRowNum() < 1) {
				throw new IllegalArgumentException("模版格式不正确，模版为2行格式");
			}
	
			Row titleRow = templateSheet.getRow(0);
	
			Iterator<Cell> cellIterator = titleRow.cellIterator();
			Row dataRow = templateSheet.getRow(1);
			cellIterator = dataRow.cellIterator();
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				dataTemplate.add(cell.getStringCellValue()); //获取XPATH参数
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(templateBook);
			IOUtils.closeQuietly(fileInputStream);
		}
	}

	public void readFromFile(String sourceFile, BatchImportSupport<T> batchImportSupport, Map<String, Object> importParam){
		FileInputStream sourceFileInputStream = null;
		Workbook sourceWorkBook = null;
		Sheet sheet = null;
		List<T> listData = new ArrayList<>();
		List<ExcelReaderError> excelReaderErrorList = new ArrayList<>();
		batchImportSupport.getImportStatusMap().put(sourceFile, 0d);
		try{
			//读取数据并保存
			sourceFileInputStream = new FileInputStream(WebContextHolder.getWarPath() + File.separator + sourceFile);
			sourceWorkBook = new HSSFWorkbook(new POIFSFileSystem(sourceFileInputStream));
			sheet = sourceWorkBook.getSheetAt(0);
			for (int r = 1; r <= sheet.getLastRowNum(); r++) { //从第2行开始读
				T rowData = getRows(sheet, r, excelReaderErrorList);
				if(rowData != null){
					listData.add(rowData);
				}
				if (rowData == null) {
					continue;
				}
				if(listData.size() == batchSize || r == sheet.getLastRowNum()){
					List<ExcelReaderError> importResult = batchImportSupport.importBatch(r - listData.size() + 1, listData ,r == sheet.getLastRowNum(), importParam);
					batchImportSupport.getImportStatusMap().put(sourceFile, ((double)r) / sheet.getLastRowNum());
					if(CollectionUtils.isNotEmpty(importResult)){
						excelReaderErrorList.addAll(importResult);
					}
					listData.clear();
					
				}
			}
		} catch (IOException|InstantiationException|IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			batchImportSupport.getImportStatusMap().put(sourceFile, Global.IMPORT_ERROR);
		} finally {
			IOUtils.closeQuietly(sourceFileInputStream);
		}
		if(sheet == null){
			return;
		}
		if(excelReaderErrorList.isEmpty()){ //导入成功，没有错误
			batchImportSupport.getImportStatusMap().put(sourceFile, Global.IMPORT_SUCCESS_ALL);
			return;
		}
		//写入错误标记数据
		
		FileOutputStream sourceOutputStream = null;
		try{
			sourceOutputStream = new FileOutputStream(WebContextHolder.getWarPath() + File.separator + sourceFile);
    		CreationHelper factory = sourceWorkBook.getCreationHelper();
			for(ExcelReaderError excelReaderError: excelReaderErrorList){
				ClientAnchor anchor = factory.createClientAnchor();
				Drawing drawing = sheet.createDrawingPatriarch();
				Comment comment = drawing.createCellComment(anchor);
				comment.setString(factory.createRichTextString(excelReaderError.getErrorText()));
				anchor.setCol1(excelReaderError.getCol());
				anchor.setCol2(excelReaderError.getCol()+3);
				anchor.setRow1(excelReaderError.getRow());
				anchor.setRow2(excelReaderError.getRow()+2);
				Cell dataCell = sheet.getRow(excelReaderError.getRow()).getCell(excelReaderError.getCol());
				if(dataCell == null){
					dataCell = sheet.getRow(excelReaderError.getRow()).createCell(excelReaderError.getCol());
				}
				if(dataCell.getCellComment() != null){
					comment.setString(factory.createRichTextString(dataCell.getCellComment().getString() + "\n" + excelReaderError.getErrorText())); //合并备注
					dataCell.removeCellComment();
				}
				dataCell.setCellComment(comment);
			}
			sourceWorkBook.write(sourceOutputStream);
			batchImportSupport.getImportStatusMap().put(sourceFile, Global.IMPORT_SUCCESS_WITH_ERROR);
		}catch (IOException e) {
			logger.error(e.getMessage(), e);
		}finally{
			IOUtils.closeQuietly(sourceOutputStream);
			IOUtils.closeQuietly(sourceWorkBook);
		}
	
	}

	private T getRows(Sheet outputSheet, int rowNum, List<ExcelReaderError> excelReaderErrorList) throws InstantiationException, IllegalAccessException{
		T rowObject = rowClass.newInstance();
    	JXPathContext objectContext = JXPathContext.newContext(rowObject);
		Row dataRow = outputSheet.getRow(rowNum);

		boolean hasError = false;
		for(int colNum = 0; colNum < dataTemplate.size(); colNum ++){
			Cell cell = dataRow.getCell(colNum);
			String cellString = readCellString(cell);
			if(StringUtils.isNotBlank(cellString)){
				String templateCellValue = dataTemplate.get(colNum);
				Matcher matcher = pattern.matcher(templateCellValue);
				if(matcher.find()){
					String xpath = matcher.group(1);
					try{
						objectContext.setValue(xpath,cellString);
					}catch(JXPathException j){
						saveExceptionLog(colNum, rowNum, j, excelReaderErrorList);
						hasError = true;
					}
				}
			}
			if(cell != null && cell.getCellComment() != null){ //把标记清理掉为错误标记做准备
				cell.removeCellComment();
			}
		}
		return hasError ? null : rowObject;
	}
	
	private void saveExceptionLog(int colNum, int rowNum, JXPathException j, List<ExcelReaderError> excelReaderErrorList){
		Throwable jex = j;
		while(jex.getCause() != null){
			jex = jex.getCause();
		}
		if(jex.getMessage() !=null){
			if(jex.getMessage().contains("For input string:")){
				excelReaderErrorList.add(new ExcelReaderError(colNum, rowNum, "数据类型错误"));
			}else{
				excelReaderErrorList.add(new ExcelReaderError(colNum, rowNum, jex.getMessage()));
			}
		}
	}
	
	private String readCellString(Cell dataCell){
		if(dataCell == null){
			return null;
		}
		switch(dataCell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC:
				return (new DataFormatter()).createFormat(dataCell).format(dataCell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return dataCell.getStringCellValue();
			default:
				return dataCell.toString();
		}
	}

	public void setBatchSize(int batchSize){
		this.batchSize = batchSize;
	}
}
