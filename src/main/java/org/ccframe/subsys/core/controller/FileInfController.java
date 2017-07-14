package org.ccframe.subsys.core.controller;

import java.io.IOException;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.data.BatchImportSupport;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.subsys.core.dto.FileInfDto;
import org.ccframe.subsys.core.service.FileInfService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ControllerMapping.ADMIN_FILE_INF_BASE)
public class FileInfController{

	@RequestMapping(value = ControllerMapping.ADMIN_FILE_INF_UPLOAD, method = RequestMethod.POST) 
	public FileInfDto upload(@RequestParam("file") MultipartFile file){
		try {
			return SpringContextHelper.getBean(FileInfService.class).saveTempFile(file.getOriginalFilename(), file.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 启动导入，由于是异步执行，直接返回
	 * @param tempFileDto
	 */
	@RequestMapping(value = ControllerMapping.ADMIN_FILE_INF_START_IMPORT, method=RequestMethod.POST)
	public String startImport(String filePath, String beanName){
		((BatchImportSupport<?>)SpringContextHelper.getBean(beanName)).doImport(filePath, null); //简单的导入不带任何参数
		return JsonBinder.buildNormalBinder().toJson(Global.SPRING_MVC_JSON_SUCCESS);
	}

	/**
	 * 查询导入的状态.
	 * 0<= x <= 1：百分比
	 * 其它状态，详见Global.java
	 * @param tempFileDto
	 * @return 进度或错误码
	 */
	@RequestMapping(value = ControllerMapping.ADMIN_FILE_INF_QUERY_IMPORT, method=RequestMethod.POST)
	public double queryImport(String filePath, String beanName){
		Double result = ((BatchImportSupport<?>)SpringContextHelper.getBean(beanName)).getImportStatusMap().get(filePath);
		return result == null ? Global.IMPORT_INIT : result;
	}

}
