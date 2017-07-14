package org.ccframe.client.commons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class UtilDateTimeClientTest {

	@Test
	public void test() throws ParseException{
		Date successDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-07-03 10:1:23");
		Date successDateTimeMm = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-07-03 10:1");
		Date successDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-07-03");
		
		//datetime
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-7-3 10:1:23"), successDateTime);
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-07-03 10:01:23"), successDateTime);
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-7-03 10:01:23"), successDateTime);
		//datetime mm
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-7-3 10:1"), successDateTimeMm);
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-07-03 10:01"), successDateTimeMm);
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-7-03 10:01"), successDateTimeMm);
		//date
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-7-03"), successDate);
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("2014-7-3"), successDate);
		//compact
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("20140703100123"), successDateTime);
		Assert.assertEquals(UtilDateTimeClient.convertStringToDateTime("20140703"), successDate);

		//convert error
		Assert.assertNull(UtilDateTimeClient.convertStringToDateTime("2014-7-03x 10:01:23"));
		Assert.assertNull(UtilDateTimeClient.convertStringToDateTime("2014-7-03-10:01:23"));
		Assert.assertNull(UtilDateTimeClient.convertStringToDateTime("20147-03 10:01:23"));
		Assert.assertNull(UtilDateTimeClient.convertStringToDateTime("2014-7-03 1001:23"));
		Assert.assertNull(UtilDateTimeClient.convertStringToDateTime("2014-07-03 99:99:99"));
		Assert.assertNull(UtilDateTimeClient.convertStringToDateTime("20143-07-03 10:01:23"));
		Assert.assertNull(UtilDateTimeClient.convertStringToDateTime("2014-17-03 10:01:23"));
	}

}
