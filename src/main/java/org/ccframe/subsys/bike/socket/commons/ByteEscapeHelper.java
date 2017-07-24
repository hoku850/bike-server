package org.ccframe.subsys.bike.socket.commons;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Bytes;

/**
 * 转义编/解码工具 0x5E， 0x5D 来代替 0x5E, 0x5E， 0x7D 来代替 0x7E
 * 
 * TODO: lqz改进算法
 * @author JIM
 *
 */
public class ByteEscapeHelper {

	private List<Byte> originalByteList = new ArrayList<Byte>();
	private List<byte[]> escapedBytesList = new ArrayList<byte[]>();

	public ByteEscapeHelper() {
	}
	
	public void addRule(byte originalByte, byte[] escapedBytes){
		originalByteList.add(originalByte);
		escapedBytesList.add(escapedBytes);
	}
	
	/**
	 * 转义编码.
	 * @param originalBytes
	 * @return
	 */
	public byte[] escapeBytes(byte[] originalBytes) {
		List<Byte> returnData = new ArrayList<Byte>();
		for (int i = 0; i < originalBytes.length; i++) {
			if (originalByteList.contains(originalBytes[i])) {
				int index = originalByteList.indexOf(originalBytes[i]); //获取当前的索引
				byte[] bs = escapedBytesList.get(index);
				for (byte b : bs) {
					returnData.add(b);
				}
			} else {
				returnData.add(originalBytes[i]);
			}
		}
		return Bytes.toArray(returnData);
	}
	
	/**
	 * 转义解码.(优化完善中...)
	 * @param escapedBytes
	 * @return
	 */
	public byte[] testNewUnescapeBytes(byte[] escapedBytes) {
		List<Byte> returnData = new ArrayList<Byte>();
		int j = 0; // 辅助拿到originalByteList的索引
		boolean isFind = true;

		for (int i = 0; i < escapedBytes.length; i++) {
 			j = 0;
 			for (byte[] esBytes : escapedBytesList) {
 				if (escapedBytes[i] != esBytes[0]) { // 判断第一个byte是否相等
					continue; // 结束该循环
				}
 				// 判断后续是否继续匹配
 				isFind = true;
 				int k;
 				for (k = 1; k < esBytes.length; k++){ 
 					if (escapedBytes[i+k] != esBytes[k]){
 						isFind = false;
 						break; // 有一个不等则不用理余下的匹配，即esBytes余下的不匹配
 					}
 				}
 				// isFind为true时表示匹配
 				if (isFind){ 
 					i = i + k; // 跳过下次的外层for循环
 					returnData.add(originalByteList.get(j));
 					//System.out.print(Integer.toHexString(originalByteList.get(j)) + " ");
 					break;
 				}
 				j++;
 			} 
 			if (i < escapedBytes.length) { // 该判断是为了i = i + k;语句溢出
 				returnData.add(escapedBytes[i]);
 				//System.out.print(Integer.toHexString(escapedBytes[i]) + " ");
			}
		}
		return Bytes.toArray(returnData);
	}
	
	/**
	 * 转义解码.
	 * @param escapedBytes
	 * @return
	 */
	public byte[] unescapeBytes(byte[] escapedBytes){
		
		List<Byte> returnData = new ArrayList<Byte>();
 		int j = 0; // 辅助拿到originalByteList的值
 		boolean isFind = false;
 		
 		for (int i = 0; i < escapedBytes.length; i++) {
			j = 0;
			isFind = false;
			for (byte[] esByte : escapedBytesList) {
				if (escapedBytes[i] == esByte[0] && escapedBytes[i+1] == esByte[1]) { //TODO lqz，写死成了2个规则，不对。
					returnData.add(originalByteList.get(j));
					isFind = true;
					i++; // 跳过下一次判断
					break;
				} 
				j++;
			}
			if (!isFind) {
				returnData.add(escapedBytes[i]);
			}
		}
		return Bytes.toArray(returnData);
	}
}