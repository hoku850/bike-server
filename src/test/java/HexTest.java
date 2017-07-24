import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class HexTest {
	public static void main(String[] args) throws Exception{
		byte[] data = Hex.decodeHex("6be8".toCharArray());
		
		int value = (short)(((data[data.length - 1]) & 0xff) + ((data[data.length - 2]) << 8));
		System.out.println(value);
		System.out.format("%x", value); //<--输出6ae8
		System.out.println("");
		System.out.println(new String(Hex.decodeHex("4e32322e35303935303500000000000000000000".toCharArray())));
		System.out.println("4e32322e35303935303500000000000000000000".toCharArray());

	}
	
	@Test
	public void testEscape() throws Exception {
		
		// mac 
		String mac = "6c47a92c4cf8";
		System.out.println(mac);
		mac = new String(Hex.decodeHex(mac.toCharArray()));
		System.out.println(mac);
		
		// 版本号
		String ver = "5731323041455f57585f56312e31303000000000";
		System.out.println(ver);
		ver = new String(Hex.decodeHex(ver.toCharArray()));
		System.out.println(ver);
		
		// IMEI
		String imei = "3436303034303737313730373933320000000000";
		System.out.println(imei);
		imei = new String(Hex.decodeHex(imei.toCharArray()));
		System.out.println(imei);
		
	}

	@Test
	public void testTrimNull() throws DecoderException {
		// 版本号
		String ver = "5731323041455f57585f56312e31303000000000";
		System.out.println(ver);

		// indexOf('\0') 从String去NUL域
		String softwareVersion = new String(Hex.decodeHex(ver.toCharArray()));
		int zeroIndex = softwareVersion.indexOf('\0');
		System.out.println(softwareVersion);
		System.out.println(softwareVersion.substring(0, zeroIndex));

		// 版本号 从byte[]去NUL域
		byte[] input = Hex.decodeHex(ver.toCharArray());
		System.out.println(Hex.encodeHexString(trimnull(input)));
	}

	/**
	 * 去除字符串中的null域 
	 * @param input
	 * @return
	 */
	public byte[] trimnull(byte[] input){
		int length = 0;
		for (int i = input.length - 1; i >= 0; i--) {
			if (input[i] != 0) break;
			length++;
		}
		return Arrays.copyOf(input, input.length - length);
	} 
}
