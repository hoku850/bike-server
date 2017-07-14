package org.ccframe.commons.util;
import org.junit.Test;

import org.junit.Assert;

public class PinYin4jUtilTest {
	@Test
	public void toPinYinFirstCharTest() {
		Assert.assertEquals(PinYin4jUtil.toPinYinFirstCharString("中华人民共和国hello长，?30X30", true), "zhrmghghelloz，?30x30");
	    System.out.println(PinYin4jUtil.toPinYinFirstCharString("CNYD", true));
	}
}
