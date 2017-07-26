import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.ccframe.subsys.bike.socket.commons.DataBlockEncodeUtil;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.junit.Before;
import org.junit.Test;


public class DataBlockEncodeUtilTest {

	Map<DataBlockTypeEnum, Object> dataBlockMap;
	
	@Before
	public void init(){
		dataBlockMap = new LinkedHashMap<>();
		dataBlockMap.put(DataBlockTypeEnum.LOCK_LNG, 113.0);
		dataBlockMap.put(DataBlockTypeEnum.LOCK_LAT, 23.0);
	}
	
	@Test
	public void encodeDataBlock() throws IOException, DecoderException, ParseException{
		String data = Hex.encodeHexString(DataBlockEncodeUtil.encodeDataBlock(dataBlockMap));
		System.out.println(data);
		System.out.println(DataBlockEncodeUtil.decodeDataBlock(Hex.decodeHex(data.toCharArray())));
	}
}
