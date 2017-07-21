package org.ccframe.subsys.bike.socket.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;

public class DataBlockEncodeUtil {
	
	private DataBlockEncodeUtil(){}
	
	public static Map<DataBlockTypeEnum, Object> decodeDataBlock(byte[] dataBuff) throws IOException{
		Map<DataBlockTypeEnum, Object> dataBlockMap = new LinkedHashMap<>();
		try (
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataBuff);
			DataInputStreamEx dataInputStream = new DataInputStreamEx(byteArrayInputStream);
		){
			while(dataInputStream.available() > 0){
				try{
					int size = dataInputStream.readByte();
					short dataBlockType = dataInputStream.readShortReverse();
					DataBlockTypeEnum dataBlockTypeEnum = DataBlockTypeEnum.fromValue(dataBlockType);
					byte[] stringBuf;
					switch(dataBlockTypeEnum){
					case LOCK_STATUS:
					case LOCK_BATTERY:
					case GPS_INFO:
					case CSQ:
					case GPRS_KEEP_ALIVE:
					case LOCK_IF_OPEN:
					case LOCK_ERROR:
					case CPRS_SATELLITE_NUM:
					case GPS_REPORT_TYPE:
					case UPGRADE:
						dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
						break;
						
					case UNLOCK_TIME_DURATION:
					case WARN_INFO:
						dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readIntReverse());
						break;
						
					case LOCK_MAC:
					case SYS_TIME:
					case SOFTWARE_VERSION:
					case IMSI:
					case USER_ID:
					case LOCK_LNG:
					case LOCK_LAT:
						stringBuf = new byte[size - 3];
						dataInputStream.read(stringBuf);
						dataBlockMap.put(dataBlockTypeEnum, Hex.encodeHexString(stringBuf));
						break;
						
					default:
						break;
					}
				}catch(ArrayIndexOutOfBoundsException e){
					break;
				}
			}
		}
		return dataBlockMap;
	}

	public static byte[] encodeDataBlock(Map<DataBlockTypeEnum, Object> dataBlockMap) throws IOException, DecoderException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024); //包大小不会超过1k
		DataOutputStreamEx dataOutputStream = new DataOutputStreamEx(byteArrayOutputStream);
		for(Entry<DataBlockTypeEnum, Object> entry: dataBlockMap.entrySet()){
			switch(entry.getKey()){
			case LOCK_STATUS:
			case LOCK_BATTERY:
			case GPS_INFO:
			case CSQ:
			case GPRS_KEEP_ALIVE:
			case LOCK_IF_OPEN:
			case LOCK_ERROR:
			case CPRS_SATELLITE_NUM:
			case GPS_REPORT_TYPE:
			case UPGRADE:
				dataOutputStream.write((byte)0x04);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.write((byte)dataBlockMap.get(entry.getKey()));
				break;
				
			case UNLOCK_TIME_DURATION:
			case WARN_INFO:
				dataOutputStream.write((byte)0x07);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.writeIntReverse((int)dataBlockMap.get(entry.getKey()));
				break;
				
			case LOCK_MAC:
			case SYS_TIME:
			case SOFTWARE_VERSION:
			case IMSI:
			case USER_ID:
			case LOCK_LNG:
			case LOCK_LAT:
				String str = (String) dataBlockMap.get(entry.getKey());
				dataOutputStream.write((byte)( str.length()/2 + 3 ));
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.write(Hex.decodeHex(str.toCharArray()));
				break;
				
			default:
				break;
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

}
