package org.ccframe.subsys.article.search;

import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleInfSearchRepository extends ElasticsearchRepository<ArticleInf, Integer>{
//	
//	public ClientPage<ArticleInfRowDto> findArticleInfList(ArticleInfListReq articleInfListReq, Integer page, Integer size){
//		
//	}
}
