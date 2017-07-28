package org.ccframe.commons.base;

/**
 * ElasticSearch 简单统计参数
 * @author JIM
 *
 */
public class AggregationField {

	public static enum AggregationType{
		MAX,MIN,AVG,SUM,COUNT
	}
	
	/**
	 * 统计字段的名字
	 */
	private String fieldName;
	
	/**
	 * 统计map输出的名字
	 */
	private String aggregationFieldName;
	
	private AggregationType aggregationType;
	
	public AggregationField(String aggregationFieldName, String fieldName, AggregationType aggregationType){
		this.aggregationFieldName = aggregationFieldName;
		this.fieldName = fieldName;
		this.aggregationType = aggregationType;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getAggregationFieldName() {
		return aggregationFieldName;
	}
	public void setAggregationFieldName(String aggregationFieldName) {
		this.aggregationFieldName = aggregationFieldName;
	}
	public AggregationType getAggregationType() {
		return aggregationType;
	}
	public void setAggregationType(AggregationType aggregationType) {
		this.aggregationType = aggregationType;
	}
}
