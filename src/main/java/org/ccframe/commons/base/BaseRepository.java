package org.ccframe.commons.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Jim Wu
 */
public interface BaseRepository<E, PK extends Serializable> extends JpaRepository<E,PK>, JpaSpecificationExecutor<E>{

/*
	private Class<E> entityClass;
    protected Logger logger = LoggerFactory.getLogger(getClass()); //NOSONAR

    //Oracle排序兼容性
    private String idFieldName;
    private Class<?> idFieldType;
    private boolean idOrderable;

    @PersistenceContext
    protected EntityManager entityManager; 
    
    @SuppressWarnings("unchecked") //NOSONAR
    public BaseJpaDao() {
        Class<?> typeCls = getClass();
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
    	idOrderable = (idFieldType != null && (
			idFieldType.isAssignableFrom(Integer.class) 
			|| idFieldType.isAssignableFrom(String.class) 
			|| idFieldType.isAssignableFrom(Long.class)
		));
    }

    protected Criteria<E> createQuery(){
    	return new Criteria<E>(entityManager, entityClass);
    }
    
	public void update(E data){
		entityManager.merge(data);
	}

	public void save(E data){
		entityManager.persist(data);
	}

	public void saveOrUpdate(E data){
		entityManager.merge(data);
	}
	
	public E getById(PK id){
		return entityManager.find(entityClass, id);
	}
	
	public void delete(E data){
		entityManager.remove(data);
	}

	public void deleteById(PK id){
		entityManager.remove(getById(id));
	}

	public List<E> listAll(){
		CriteriaQuery<E> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass); //定义返回类型E
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	public List<E> findByKey(String fieldName, Object value){
		return findByKey(fieldName, value, new QueryOrder[]{});
	}

	public List<E> findByKey(String fieldName, Object value, QueryOrder... queryOrders){
		Criteria<E> criteria = this.createQuery().add(Restrictions.eq(fieldName, value));
		if(queryOrders.length > 0){
			criteria.addOrder(queryOrders);
		}
		return criteria.list();
	}

	public E getByKey(String fieldName, Object value){
		return getByKey(fieldName, value, new QueryOrder[]{});
	}

	public E getByKey(String fieldName, Object value, QueryOrder... queryOrders){
		Criteria<E> criteria = this.createQuery().add(Restrictions.eq(fieldName, value));
		if(queryOrders.length > 0){
			criteria.addOrder(queryOrders);
		}
		return criteria.first();
	}

	protected Page<E> pageQuery(Predicate predicate, Integer offset, Integer limit){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(entityClass)));
		if(predicate != null){
			countQuery.where(predicate);
		}
		Long count = entityManager.createQuery(countQuery).getSingleResult(); //除非100%确认只有一个返回，否则不要用getSingleResult
		
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		if(predicate != null){
			criteriaQuery.where(predicate);
		}
		List<E> resultList = entityManager.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(limit).getResultList();
		return new Page<E>(count.intValue(), offset,limit, resultList);
	}
*/
}

