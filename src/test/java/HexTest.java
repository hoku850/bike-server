import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.alibaba.druid.sql.visitor.functions.If;

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
	public void testName() throws Exception {
		
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
		System.out.println(trimnull(imei));
	}
	
	/** 
     * 去除字符串中的null域 
     * @param string 
     * @return 
     * @throws UnsupportedEncodingException  
     */  
    public String trimnull(String string) throws UnsupportedEncodingException  
    {  
        ArrayList<Byte> list = new ArrayList<Byte>();  
        byte[] bytes = string.getBytes("UTF-8");  
        for(int i=0;bytes!=null&&i<bytes.length;i++){  
            if(0!=bytes[i]){  
                list.add(bytes[i]);
            }  
        }  
        byte[] newbytes = new byte[list.size()];  
        for(int i = 0 ; i<list.size();i++){  
            newbytes[i]=(Byte) list.get(i);   
        }  
        String str = new String(newbytes,"UTF-8");  
        return str;  
    }  

}
