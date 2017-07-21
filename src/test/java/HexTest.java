import org.apache.commons.codec.binary.Hex;

public class HexTest {
	public static void main(String[] args) throws Exception{
		byte[] data = Hex.decodeHex("6be8".toCharArray());
		
		int value = (short)(((data[data.length - 1]) & 0xff) + ((data[data.length - 2]) << 8));
		System.out.println(value);
		System.out.format("%x", value); //<--输出6ae8
	}
}
