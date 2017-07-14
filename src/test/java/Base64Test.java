
import org.junit.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Test {
	
	@Test
	public void toBase64() throws Exception{
		String[] values = new String[] {
				"飞机"
		};
		BASE64Encoder encoder = new BASE64Encoder();
		for(String value: values){
			String encodeStr = encoder.encode(value.getBytes("UTF-8"));
            BASE64Decoder decoder = new BASE64Decoder();
			System.out.println("编码串:" + encodeStr + "\n");
            System.out.println("解码串:" + new String(decoder.decodeBuffer(encodeStr),"UTF-8") + "\n");
		}
		
//		BASE64Decoder decoder = new BASE64Decoder();
//		System.out.println(new String(decoder.decodeBuffer("5Y+R54OnLOaEn+WGkizlkrPll70s5rWB5oSf"),"UTF-8"));
	}
}
