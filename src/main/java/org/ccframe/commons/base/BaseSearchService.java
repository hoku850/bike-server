package org.ccframe.commons.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

public abstract class BaseSearchService <E extends Serializable,ID extends Serializable,R extends ElasticsearchRepository<E,ID>> implements IHasSearchBuilder<E, ID>{ //spring 4.X 支持泛型注入
	
	private static final int INDEX_LOG_STEP = 100;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private R repository;

    private Class<E> entityClass;
    private String idFieldName;
    private Class<?> idFieldType;

    private static final int FIND_BY_KEY_MAX = 10000; //result_window最大值
    
	@SuppressWarnings("unchecked") //NOSONAR
    public BaseSearchService() {
        Class typeCls = getClass();
        Type genType = typeCls.getGenericSuperclass();
        while (true) {
            if (!(genType instanceof ParameterizedType)) {
                typeCls = typeCls.getSuperclass();
                genType = typeCls.getGenericSuperclass();
            } else {
                break;
            }
        }
        this.entityClass = (Class<E>) ((ParameterizedType) genType).getActualTypeArguments()[0];
        
    	Field[] fields = entityClass.getDeclaredFields();
    	for(Field field: fields){
    		if(field.isAnnotationPresent(Id.class)){
    			idFieldName = field.getName();
    			idFieldType = field.getType();
    		}
    	}
    }

	@Autowired
	public void setRepository(R repository) {
		this.repository = repository;
	}

	protected R getRepository(){
		return repository;
	}

	@Transactional(readOnly = true)
	public E getById(ID id) {//
		return getRepository().findOne(id);
	}
	
	@Transactional(readOnly = true)
	public Iterable<E> listAll() {
		return getRepository().findAll();
	}
	
	@Transactional
	public void save(E data){
		getRepository().save(data);
	}

	@Transactional
	public void delete(E data){
		getRepository().delete(data);
	}

	@Transactional
	public void deleteById(ID id){
		getRepository().delete(id);
	}

	@Async
	@Override
	@Transactional
	public void buildAllIndex() {
		List<ID> idList = getJpaService().findIdList();
		List<E> batchList = new ArrayList<E>();
		for(int i = 1; i <= idList.size(); i ++){
			batchList.add(getJpaService().getById(idList.get(i - 1)));
			if(i == idList.size()){
				this.getRepository().save(batchList); //批量建立索引
				log.info("索引进度: " + entityClass.getSimpleName() + " "+ i +"/"+idList.size());
			}else if(i % INDEX_LOG_STEP == 0){
				this.getRepository().save(batchList); //批量建立索引
				batchList.clear();
				log.info("索引进度: " + entityClass.getSimpleName() + " "+ i +"/"+idList.size());
			}
		}
	}

	
	
	@Transactional(readOnly = true)
	public List<E> findByKey(String fieldName, Object value, Order... orders){
		MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery(fieldName, value).boost(100);
		if(orders.length == 0){
			return Lists.newArrayList(this.getRepository().search(queryBuilder));
		}else{
			Page<E> resultPage = this.getRepository().search(queryBuilder, new PageRequest(0, FIND_BY_KEY_MAX, new Sort(orders)));
			return resultPage.getContent();
		}
	}

	@Transactional(readOnly = true)
	public E getByKey(String fieldName, Object value){
		try{
			return getRepository().search(QueryBuilders.matchQuery(fieldName, value)).iterator().next();
		}catch(NoSuchElementException e){
			return null;
		}
	}

	/**
	 * 如果async线程池不多，且并发线程要调优，可以将数据最多的表service适当提高优先级以提高线程利用率
	 * @see org.ccframe.commons.base.IHasSearchBuilder#getPriority()
	 */
	@Override
	public Integer getPriority() {
		return 100;
	}

	public BaseService<E, ID, ?> getJpaService() {
		String beanName = StringUtils.uncapitalize(getClass().getSimpleName().replace(Global.SEARCH_SERVICE_CLASS_SUFFIX, Global.SERVICE_CLASS_SUFFIX));
		return (BaseService<E, ID, ?>)SpringContextHelper.getBean(beanName);
	}

}
