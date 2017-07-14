package org.ccframe.subsys.article.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.FileUrlHelper;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.article.dto.ArticleInfDto;
import org.ccframe.subsys.article.dto.ArticleInfListReq;
import org.ccframe.subsys.article.dto.ArticleInfRowDto;
import org.ccframe.subsys.article.service.ArticleInfSearchService;
import org.ccframe.subsys.article.service.ArticleInfService;
import org.ccframe.subsys.core.domain.code.BusinessTypeCodeEnum;
import org.ccframe.subsys.core.domain.entity.FileInf;
import org.ccframe.subsys.core.dto.FileInfDto;
import org.ccframe.subsys.core.service.FileInfService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.ARTICLE_BASE)
public class ArticleInfController{
	
	@RequestMapping(value = ControllerMapping.ARTICLE_LIST, method = RequestMethod.POST)
	public ClientPage<ArticleInfRowDto> findArticleInfList(@RequestBody ArticleInfListReq articleInfListReq, int page, int size, HttpServletRequest request) {
//		return SpringContextHelper.getBean(ArticleInfService.class).findArticleInfList(articleInfListReq, page, size);
		return SpringContextHelper.getBean(ArticleInfSearchService.class).findArticleInfList(articleInfListReq, page, size);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH)
	public ArticleInfDto getById(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer articleInfId){
		ArticleInfDto articleInfDto = new ArticleInfDto();
		BeanUtils.copyProperties(SpringContextHelper.getBean(ArticleInfService.class).getById(articleInfId), articleInfDto);
		List<FileInf> fileInfList = SpringContextHelper.getBean(FileInfService.class).findByBusinessTypeCodeBusinessObjectId(BusinessTypeCodeEnum.ARTICLE, articleInfId);
		List<FileInfDto> fileInfBarDtoList = new ArrayList<FileInfDto>();
		for(FileInf fileInf: fileInfList){
			FileInfDto fileInfBarDto = new FileInfDto();
			BeanUtils.copyProperties(fileInf, fileInfBarDto);
			fileInfBarDto.setFileUrl(FileUrlHelper.getFileSystemUrl(fileInf.getFilePath()));
			fileInfBarDtoList.add(fileInfBarDto);
		}
		articleInfDto.setFileInfBarDtoList(fileInfBarDtoList);
		return articleInfDto;
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdateArticleInfDto(@RequestBody ArticleInfDto articleInfDto){
		ArticleInf articleInf = SpringContextHelper.getBean(ArticleInfService.class).saveOrUpdateArticleInfDto(articleInfDto);
		//必须在保证事务正常执行的情况下，才提交索引
		SpringContextHelper.getBean(ArticleInfSearchService.class).save(articleInf);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer articleInfId){
		SpringContextHelper.getBean(ArticleInfService.class).deleteById(articleInfId);
		//必须在保证事务正常执行的情况下，才提交索引
		SpringContextHelper.getBean(ArticleInfSearchService.class).deleteById(articleInfId);
	}
}
