import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.ccframe.commons.util.CRC16Util;
import org.junit.Test;

//import sun.misc.CRC16;

public class CRC16UtilTest {
	
	@Test
	public void test() throws DecoderException {
        // 一个测试样本数据  
//    	byte[] p = Hex.decodeHex("01014b0100324b00000001040004040101075200040000000a5001201706061808010410000204500000".toCharArray());
//        int crc = CRC16Util.crc16(p); // 计算前两位的CRC码  
//		Assert.assertEquals(crc, 0xa0a8);
		System.out.format("%x", CRC16Util.crc16(Hex.decodeHex("01014b0100324b0000000106ff041000010451004f0905016c47a92c4cf80a500120170606144159045300001708005731323041455f57585f56312e31303000000000".toCharArray())));
	}
}
