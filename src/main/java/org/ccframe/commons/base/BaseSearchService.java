package org.ccframe.commons.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

public abstract class BaseSearchService <E extends Serializable,ID extends Serializable,R extends ElasticsearchRepository<E,ID>> implements IHasSearchBuilder<E, ID>{ //spring 4.X 支持泛型注入
	
	private static final int INDEX_LOG_STEP = 1000;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private R repository;

    private Class<E> entityClass;
    private String idFieldName;
    private Class<?> idFieldType;

    private static final int FIND_BY_KEY_MAX = 10000; //result_window最大值
    
    //简单统计的字段前缀约定
	private static final String MAX_OF_PERFIX = "maxOf";
	private static final String MIN_OF_PERFIX = "minOf";
	private static final String AVG_OF_PERFIX = "avgOf";
	private static final String SUM_OF_PERFIX = "sumOf";
	private static final String COUNT_OF_PERFIX = "countOf";
	
	private static final String Aggregation_FIELD = "aggregation_";
    
    private ElasticsearchTemplate elasticsearchTemplate;
    
	public void setElasticsearchTemplate(ElasticsearchTemplate elasticsearchTemplate) {
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public ElasticsearchTemplate getElasticsearchTemplate() {
		return elasticsearchTemplate;
	}

	
	public Double sumQuery(QueryBuilder queryBuilder,String fieldName){
		return aggregationQuery(
			queryBuilder, 
			new AggregationField[]{new AggregationField(Aggregation_FIELD + fieldName, fieldName, AggregationField.AggregationType.SUM)}
		).get(Aggregation_FIELD + fieldName);
	}

	public Double maxQuery(QueryBuilder queryBuilder,String fieldName){
		return aggregationQuery(
			queryBuilder, 
			new AggregationField[]{new AggregationField(Aggregation_FIELD + fieldName, fieldName, AggregationField.AggregationType.MAX)}
		).get(Aggregation_FIELD + fieldName);
	}

	public Double minQuery(QueryBuilder queryBuilder,String fieldName){
		return aggregationQuery(
			queryBuilder, 
			new AggregationField[]{new AggregationField(Aggregation_FIELD + fieldName, fieldName, AggregationField.AggregationType.MIN)}
		).get(Aggregation_FIELD + fieldName);
	}

	public Double avgQuery(QueryBuilder queryBuilder,String fieldName){
		return aggregationQuery(
			queryBuilder, 
			new AggregationField[]{new AggregationField(Aggregation_FIELD + fieldName, fieldName, AggregationField.AggregationType.AVG)}
		).get(Aggregation_FIELD + fieldName);
	}

	public Long countQuery(QueryBuilder queryBuilder,String fieldName){
		return aggregationQuery(
			queryBuilder, 
			new AggregationField[]{new AggregationField(Aggregation_FIELD + fieldName, fieldName, AggregationField.AggregationType.COUNT)}
		).get(Aggregation_FIELD + fieldName).longValue();
	}

	/**
	 * 执行统计请求，注意count和整数字段的sum等也是使用double来处理，返回时要注意类型处理.
	 * @param queryBuilder
	 * @param aggregationFields
	 * @return
	 */
	public Map<String, Double> aggregationQuery(QueryBuilder queryBuilder,final AggregationField[] aggregationFields){
		
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
			.withQuery(queryBuilder)
			.withSearchType(SearchType.DEFAULT)
			.withIndices(Global.ES_DEFAULT_INDEX)
			.withTypes(StringUtils.uncapitalize(entityClass.getSimpleName())); //ES类型名用于path，因此首字符是小写

		for(AggregationField aggregationField: aggregationFields){
			switch(aggregationField.getAggregationType()){
				case MAX:
					builder.addAggregation(AggregationBuilders.max(aggregationField.getAggregationFieldName()).field(aggregationField.getFieldName()));
					break;
				case MIN:
					builder.addAggregation(AggregationBuilders.min(aggregationField.getAggregationFieldName()).field(aggregationField.getFieldName()));
					break;
				case AVG:
					builder.addAggregation(AggregationBuilders.avg(aggregationField.getAggregationFieldName()).field(aggregationField.getFieldName()));
					break;
				case SUM:
					builder.addAggregation(AggregationBuilders.sum(aggregationField.getAggregationFieldName()).field(aggregationField.getFieldName()));
					break;
				case COUNT:
					builder.addAggregation(AggregationBuilders.count(aggregationField.getAggregationFieldName()).field(aggregationField.getFieldName()));
					break;
				default:
			}
		}
		return elasticsearchTemplate.query(builder.build(), new ResultsExtractor<Map<String, Double>>() {
			@Override
			public Map<String, Double> extract(SearchResponse response) {
				Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
				
				Map<String, Double> resultMap = new HashMap<String, Double>();
				for (AggregationField aggregationField: aggregationFields) {
						switch(aggregationField.getAggregationType()){
						case MAX:
							resultMap.put(aggregationField.getAggregationFieldName(), ((Max)aggregationMap.get(aggregationField.getAggregationFieldName())).getValue());
							break;
						case MIN:
							resultMap.put(aggregationField.getAggregationFieldName(), ((Min)aggregationMap.get(aggregationField.getAggregationFieldName())).getValue());
							break;
						case AVG:
							resultMap.put(aggregationField.getAggregationFieldName(), ((Avg)aggregationMap.get(aggregationField.getAggregationFieldName())).getValue());
							break;
						case SUM:
							resultMap.put(aggregationField.getAggregationFieldName(), ((Sum)aggregationMap.get(aggregationField.getAggregationFieldName())).getValue());
							break;
						case COUNT:
							resultMap.put(aggregationField.getAggregationFieldName(), Long.valueOf(((ValueCount)aggregationMap.get(aggregationField.getAggregationFieldName())).getValue()).doubleValue());
							break;
						default:
					}
				}
				return resultMap;
			}
		});		
	}

	/**
	 * 简单统计查询，通常用于分页列表底部的汇总.
	 * 
	 * classOfAggregationObj需要具备字段前缀约定的字段，系统会自动根据这些字段进行汇总处理.
	 * 例如classOfAggregationObj有一个countOfUserId，那么查询就会针对这个字段进行汇总后返回到该值。也就是UserId不为NULL的符合条件的总数
	 * 
	 * @param queryBuilder
	 * @param fieldNames
	 * @return
	 */
	public Object aggregationQuery(QueryBuilder queryBuilder,Object classOfAggregationObj){
		
		//TODO 继续开发，使用简单统计的字段前缀约定反射来填充对象字段

//		for(PropertyDescriptor propertyDescriptors: BeanUtils.getPropertyDescriptors(classOfAggregationObj.getClass())){
//			System.out.println(propertyDescriptors.getName());
//		};
//		StringUtils.uncapitalize(str);
		
		return null;
	
		
	}

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
