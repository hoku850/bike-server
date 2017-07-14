package org.ccframe.commons.util;

import org.ccframe.commons.util.BigDecimalUtil;

import org.junit.Assert;
import org.junit.Test;

public class BigDecimalUtilTest {
	
	@Test
	public void test(){
		Assert.assertEquals(BigDecimalUtil.multiply(new Double("33.33543342") ,new Double("333.22223234")), new Double(11108.10754023));
	}
}
