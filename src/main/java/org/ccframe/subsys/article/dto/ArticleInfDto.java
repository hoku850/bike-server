package org.ccframe.subsys.article.dto;

import java.util.List;

import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.core.dto.FileInfDto;

public class ArticleInfDto extends ArticleInf{
	private static final long serialVersionUID = 5845798144463985712L;

	List<FileInfDto> fileInfBarDtoList;
	
	public List<FileInfDto> getFileInfBarDtoList() {
		return fileInfBarDtoList;
	}
	
	public void setFileInfBarDtoList(List<FileInfDto> fileInfBarDtoList) {
		this.fileInfBarDtoList = fileInfBarDtoList;
	}
	
}
