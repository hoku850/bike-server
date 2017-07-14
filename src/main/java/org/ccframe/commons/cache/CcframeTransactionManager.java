package org.ccframe.commons.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.TransactionController;

import org.ccframe.commons.util.ElasticsearchTransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * 完成缓存、索引的事务同步。
 * 注：如果要即时生效，请单独调用ehcache来刷新缓存或用ElasticsearchTransactionUtil来刷新索引
 * @author JIM
 */
public class CcframeTransactionManager extends JpaTransactionManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3878501009638970644L;

	private CacheManager ehcache;

	@Autowired
	public void setEhcache(CacheManager ehcache) {
		this.ehcache = ehcache;
	}

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		super.doBegin(transaction, definition);
		ehcache.getTransactionController().begin(120);
		if(!definition.isReadOnly()){ //只读事务无需操作索引
			ElasticsearchTransactionUtil.init();
		}
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		super.doCommit(status);
		TransactionController controller = ehcache.getTransactionController();
		if (controller.getCurrentTransactionContext() != null) {
			if(status.isRollbackOnly()){
				controller.rollback(); //在rollback only异常下强制rollback
			}
			controller.commit();
		}
		if(!status.isReadOnly()){ //只读事务无需操作索引
			ElasticsearchTransactionUtil.commit();
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		super.doRollback(status);
		TransactionController controller = ehcache.getTransactionController();
		if (controller.getCurrentTransactionContext() != null) {
			controller.rollback();
		}
		if(!status.isReadOnly()){ //只读事务无需操作索引
			ElasticsearchTransactionUtil.rollback();
		}
	}

}
