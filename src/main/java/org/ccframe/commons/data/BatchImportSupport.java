package org.ccframe.commons.data;

import java.util.List;
import java.util.Map;

/**
 * 支持批量导入的Service类接口
 * @author JIM
 *
 */
public interface BatchImportSupport<T> {
	
	/**
	 * 批量导入数据
	 * @param importList 从EXCEL读取的导入数据对象
	 * @return 返回导入的错误列表，用于excel进行标记
	 */
	List<ExcelReaderError> importBatch(int rowBase, List<T> importList ,boolean isLastRow, Map<String, Object> importParam);

	/**
	 * 执行导入的接口.
	 * @param FilePath
	 */
	void doImport(String FilePath, Map<String, Object> importParam);

	/**
	 * 获得导入进度状态map，map的key是tempFilePath
	 * @return
	 */
	Map<String, Double> getImportStatusMap();
	
}
