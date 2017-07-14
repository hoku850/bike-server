package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.jpaquery.Criteria;
import org.ccframe.commons.jpaquery.Restrictions;
import org.ccframe.subsys.core.domain.entity.Param;
import org.ccframe.subsys.core.dto.ParamRowDto;
import org.ccframe.subsys.core.repository.ParamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParamService extends BaseService<Param, Integer, ParamRepository> {

	@Transactional(readOnly = true)
	public String getParamValue(String paramInnerCoding){
		Param param = getByKey(Param.PARAM_INNER_CODING, paramInnerCoding);
		return param == null ? null : param.getParamValueStr();
	}
	
	@Transactional
	public void setParamValue(String paramInnerCoding, String paramValue){
		Param param = getByKey(Param.PARAM_INNER_CODING, paramInnerCoding);
		param.setParamValueStr(paramValue);
	}

	@Transactional(readOnly = true)
	public List<ParamRowDto> findUserListByNameOrInnerCode(String paramNameOrInnerCode) {
		List<Param> paramList;
		if(StringUtils.isBlank(paramNameOrInnerCode)){
			paramList = this.listAll();
		}else{
			paramList = this.getRepository().findAll(new Criteria().add(Restrictions.or(Restrictions.like(Param.PARAM_NM, "%" + paramNameOrInnerCode + "%"), Restrictions.like(Param.PARAM_INNER_CODING, "%" + paramNameOrInnerCode + "%"))));
		}
		List<ParamRowDto> resultList = new ArrayList<ParamRowDto>();
		for(Param param :paramList){
			ParamRowDto paramRowDto = new ParamRowDto();
			paramRowDto.setParamId(param.getParamId());
			paramRowDto.setParamInnerCoding(param.getParamInnerCoding());
			paramRowDto.setParamTypeCode(param.getParamTypeCode());
			paramRowDto.setParamNm(param.getParamNm());
			paramRowDto.setParamValueStr(StringUtils.substring(param.getParamValueStr(), 0, 50)); //列表限下传50个字
			resultList.add(paramRowDto);
		}
		return resultList;
	}

	@Transactional
	public void setParamValue(Integer paramId, String paramValue) {
		Param param = this.getById(paramId);
		param.setParamValueStr(paramValue);
		save(param);
	}
}
