package org.ccframe.commons.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

import org.ccframe.commons.helper.EhCacheHelper;
import org.ccframe.commons.jpaquery.Criteria;
import org.ccframe.commons.jpaquery.Restrictions;
import org.ccframe.commons.util.ElasticsearchTransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseService <E extends Serializable,PK extends Serializable,R extends BaseRepository<E,PK>>{ //spring 4.X 支持泛型注入
	
//	private Log log = LogFactory.getLog(getClass());
    private Class<E> entityClass;
    private String idFieldName;
    private Class<?> idFieldType;
    
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private EhCacheHelper ehCacheHelper;
	
    @SuppressWarnings("unchecked") //NOSONAR
    public BaseService() {
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
  
    public Class<E> getEntityClass(){
    	return entityClass;
    }
    
	@Transactional(readOnly = true)
	public List<PK> findIdList(){
		return entityManager.createQuery("select o." + idFieldName + " from " + entityClass.getSimpleName() + " o").getResultList();
	}
	
	private R repository;
	
	@Autowired
	public void setRepository(R repository) {
		this.repository = repository;
	}

	protected R getRepository(){
		return repository;
	}

	@Transactional(readOnly = true)
	public E getById(PK id) {//
		return getRepository().findOne(id);
	}
	
	@Transactional(readOnly = true)
	public List<E> listAll() {
		return getRepository().findAll();
	}
	
	@Transactional
	public E save(E data){
		E result = getRepository().save(data);
		ElasticsearchTransactionUtil.pushSave(this.getClass(), result);
		return result;
	}
	
	@Transactional
	public E saveAndFlush(E data){
		E result = getRepository().saveAndFlush(data);
		ehCacheHelper.transactionCommit();
        ElasticsearchTransactionUtil.pushSave(this.getClass(), data);
        ElasticsearchTransactionUtil.commit();
        return result;
	}

	@Transactional
	public void delete(E data){
		getRepository().delete(data);
        ElasticsearchTransactionUtil.pushDelete(this.getClass(), data);
	}

	@Transactional
	public void deleteById(PK id){
		getRepository().delete(id);
        ElasticsearchTransactionUtil.pushDeleteById(this.getClass(), id);
	}

	@Transactional(readOnly = true)
	public List<E> findByKey(String fieldName, Object value, Order... orders){
		if(orders.length == 0){
			return getRepository().findAll(new Criteria<E>().add(Restrictions.eq(fieldName, value)));
		}else{
			return getRepository().findAll(new Criteria<E>().add(Restrictions.eq(fieldName, value)), new Sort(orders));
		}
	}

	@Transactional(readOnly = true)
	public E getByKey(String fieldName, Object value){
		return getRepository().findOne(new Criteria<E>().add(Restrictions.eq(fieldName, value)));
	}
}
