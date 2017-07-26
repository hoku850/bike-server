import org.ccframe.subsys.bike.socket.commons.ByteEscapeHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ByteEscapeHelperTest {

	// 0x5E， 0x5D 来代替 0x5E, 0x5E， 0x7D 来代替 0x7E
	byte[] originalBytes = new byte[] { 0x34, 0x5E, 0x5D, 0x7D, 0x73, 0x7E,	0x7D, 0x5e, 0x7D };

	ByteEscapeHelper byteEscapeHelper = new ByteEscapeHelper();;

	@Before
	public void init() {
		byteEscapeHelper.addRule((byte) 0x5e, new byte[] { 0x5E, 0x5D });
		byteEscapeHelper.addRule((byte) 0x7e, new byte[] { 0x5E, 0x7D });
	}

	@Test
	public void testEscapeBytes() throws Exception {
		byte[] escapeBytes = byteEscapeHelper.escapeBytes(originalBytes);
		for (int i = 0; i < escapeBytes.length; i++) {
			System.out.print(Integer.toHexString((byte) escapeBytes[i]) + " ");
		}
	}

	@Test
	public void testUnescapeBytes() throws Exception {

		System.out.println("0x5E， 0x5D 来代替 0x5E, 0x5E， 0x7D 来代替 0x7E");
		for (int i = 0; i < originalBytes.length; i++) {
			System.out.print(Integer.toHexString((byte) originalBytes[i]) + " ");
		}
		System.out.println();
		
		byte[] unescapeBytes = byteEscapeHelper.unescapeBytes(byteEscapeHelper.escapeBytes(originalBytes));
		for (int i = 0; i < unescapeBytes.length; i++) {
			System.out.print(Integer.toHexString((byte) unescapeBytes[i]) + " ");
		}
		System.out.println();
	}
}
