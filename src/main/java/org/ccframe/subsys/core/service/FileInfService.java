package org.ccframe.subsys.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.FileUrlHelper;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.jpaquery.Criteria;
import org.ccframe.commons.jpaquery.Restrictions;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.BusinessTypeCodeEnum;
import org.ccframe.subsys.core.domain.entity.FileInf;
import org.ccframe.subsys.core.dto.FileInfDto;
import org.ccframe.subsys.core.dto.FileInfListReq;
import org.ccframe.subsys.core.repository.FileInfRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileInfService extends BaseService<FileInf, Integer, FileInfRepository> {

	@Transactional(readOnly = true)
	public List<FileInf> findSysFileInfList(FileInfListReq fileInfListReq) {
		return this.getRepository().findAll(
			new Criteria<FileInf>()
			.add(Restrictions.eq(FileInf.BUSINESS_TYPE_CODE, fileInfListReq.getBusinessTypeCode()))
			.add(Restrictions.eq(FileInf.IF_FAIL_USE, fileInfListReq.getIfFailUse()))
			.add(Restrictions.eq(FileInf.BUSINESS_OBJECT_ID, fileInfListReq.getBusinessObjectId()))
			.add(Restrictions.like(FileInf.FILE_NM, fileInfListReq.getFileNm()))
		);
	}

	@Transactional(readOnly = true)
	public List<FileInf> findByBusinessTypeCodeBusinessObjectId(BusinessTypeCodeEnum businessTypeCode, Integer businessObjectId) {
		return findByBusinessTypeCodeBusinessObjectId(businessTypeCode, businessObjectId, false);
	}

	@Transactional(readOnly = true)
	public List<FileInf> findByBusinessTypeCodeBusinessObjectId(BusinessTypeCodeEnum businessTypeCode, Integer businessObjectId, boolean ifFailUse) {
		FileInfListReq fileInfListReq = new FileInfListReq();
		fileInfListReq.setBusinessTypeCode(businessTypeCode.toCode());
		fileInfListReq.setBusinessObjectId(businessObjectId);
		fileInfListReq.setIfFailUse(BoolCodeEnum.toCode(ifFailUse));
		return findSysFileInfList(fileInfListReq);
	}

	/**
	 * 将文件保存到临时目录.
	 * @param fileName
	 * @param fileStream
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@Transactional
	public FileInfDto saveTempFile(String fileName, InputStream fileStream) throws FileNotFoundException, UnsupportedEncodingException{
		FileOutputStream fileOutputStream = null;
		try{
			String fileTypeNm = FilenameUtils.getExtension(fileName);
			String fileUUID = UUID.randomUUID().toString();
			String filePath = new StringBuilder(Global.EXCEL_EXPORT_TEMP_DIR).append(File.separator).append(fileUUID).append(".").append(fileTypeNm).toString();
			StringBuilder outputFilePath = new StringBuilder(WebContextHolder.getWarPath()).append(File.separator).append(filePath);
			fileOutputStream = new FileOutputStream(outputFilePath.toString());
			IOUtils.copy(fileStream, fileOutputStream);
			FileInfDto fileInfBarDto = new FileInfDto();
			fileInfBarDto.setFileNm(fileName);
			fileInfBarDto.setFilePath(filePath);
			fileInfBarDto.setFileTypeNm(fileTypeNm);
			fileInfBarDto.setFileUrl(new StringBuilder(WebContextHolder.getContextPath()).append("/").append(Global.EXCEL_EXPORT_TEMP_DIR).append("/").append(fileUUID).append(".").append(fileTypeNm).toString());
			return fileInfBarDto;
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}finally{
			if(fileOutputStream != null){
				IOUtils.closeQuietly(fileOutputStream);
			}
		}
	}
	
	/**
	 * 同步某个业务下的资源文件集合.
	 * 如果ID不在已有里的，就添加；如果有在的，就不管；如果不在的，就删除(标记为failUse，等整理时再批量删除)。
	 * @param businessTypeCode
	 * @param fileInfBarDtoList
	 * @return 替换用的map，将文档内容里的temp url替换为upload url
	 */
	@Transactional
	public Map<String, String> syncFileInfList(BusinessTypeCodeEnum businessTypeCode, Integer businessObjectId, List<FileInfDto> fileInfBarDtoList){
		List<FileInf> savedFileInfList = this.findByBusinessTypeCodeBusinessObjectId(businessTypeCode, businessObjectId);
		
		List<Integer> savedFileInfIdList = new ArrayList<Integer>();
		for(FileInf savedFileInf : savedFileInfList){
			savedFileInfIdList.add(savedFileInf.getFileInfId());
		}
		
		List<FileInfDto> addFileInfBarDto = new ArrayList<FileInfDto>();
		
		List<Integer> updateFileInfIdList = new ArrayList<Integer>();
		for(FileInfDto fileInfBarDto : fileInfBarDtoList){
			if(fileInfBarDto.getFileInfId() < 0){ //新增
				addFileInfBarDto.add(fileInfBarDto);
			}else{
				updateFileInfIdList.add(fileInfBarDto.getFileInfId());
			}
		}
		
		Collection<Integer> deleteList = CollectionUtils.subtract(savedFileInfIdList, updateFileInfIdList);
		for(Integer deleteId: deleteList){
			FileInf toDelete = this.getById(deleteId);
			toDelete.setIfFailUse(BoolCodeEnum.YES.toCode());
			save(toDelete); //软删除
		}
		
		Map<String, String> result = new HashMap<String, String>();
		Date now = new Date();
		for(FileInfDto fileInfBarDto: addFileInfBarDto){
			File tempFile = new File(WebContextHolder.getWarPath() + File.separator + fileInfBarDto.getFilePath());
			Calendar calender = Calendar.getInstance();
			
			String moveDirPath = new StringBuilder(Global.UPLOAD_DIR)
				.append(File.separator)
				.append(calender.get(Calendar.YEAR))
				.append(File.separator)
				.append(calender.get(Calendar.MONTH))
				.append(File.separator)
				.append(calender.get(Calendar.DAY_OF_MONTH)).toString();
			try {
				FileUtils.moveFileToDirectory(tempFile, new File(WebContextHolder.getWarPath() + File.separator + moveDirPath), true);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			FileInf fileInf = new FileInf();
			fileInf.setFileNm(fileInfBarDto.getFileNm());
			fileInf.setFileTypeNm(fileInfBarDto.getFileTypeNm());
			fileInf.setIfFailUse(BoolCodeEnum.NO.toCode());
			fileInf.setFileTime(now);
			fileInf.setFilePath(moveDirPath + File.separator + FilenameUtils.getName(fileInfBarDto.getFilePath()));
			fileInf.setBusinessObjectId(businessObjectId);
			fileInf.setBusinessTypeCode(businessTypeCode.toCode());
			SpringContextHelper.getBean(this.getClass()).save(fileInf);
			result.put(fileInfBarDto.getFileUrl(), FileUrlHelper.getFileSystemUrl(fileInf.getFilePath()));
		}
		return result;
	}

}
