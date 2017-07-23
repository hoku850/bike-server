import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.ccframe.subsys.bike.socket.commons.ByteEscapeHelper;
import org.junit.Before;
import org.junit.Test;

public class ByteEscapeHelperTest {

	//0x5E， 0x5D 来代替 0x5E, 0x5E， 0x7D 来代替 0x7E
	byte[] originalBytes = new byte[]{0x34, 0x5E, 0x5D, 0x7D, 0x73, 0x7E, 0x7D, 0x5e, 0x7D};
	
	ByteEscapeHelper byteEscapeHelper;
	
	@Test
	public void test4() throws Exception {
		Arrays.fill(originalBytes, 0, 3, (byte)0x43);
		System.out.println(Hex.encodeHexString(originalBytes));
	}
	
	@Before
	public void init() {
		byteEscapeHelper = new ByteEscapeHelper();
		byteEscapeHelper.addRule((byte)0x5e, new byte[]{0x5E, 0x5D});
		byteEscapeHelper.addRule((byte)0x7e, new byte[]{0x5E, 0x7D});
	}
	
	@Test
	public void testEscapeBytes() throws Exception {
		byte[] escapeBytes = byteEscapeHelper.escapeBytes(originalBytes);
		for (int i = 0; i < escapeBytes.length; i++) {
			System.out.print(Integer.toHexString((byte) escapeBytes[i]) + " ");
		}
//		System.out.println(Hex.encodeHexString(escapeBytes));
	}
	
	@Test
	public void testUnescapeBytes() throws Exception {
		byte[] unescapeBytes = byteEscapeHelper.unescapeBytes(originalBytes);
		for (int i = 0; i < unescapeBytes.length; i++) {
			System.out.print(Integer.toHexString((byte) unescapeBytes[i])+ " ");
		}
//		System.out.println(Hex.encodeHexString(unescapeBytes));
	}
}
