package org.ccframe.subsys.bike.socket.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.ccframe.commons.util.UtilDateTime;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;

public class DataBlockEncodeUtil {
	
	private static final int HEAD_LENGTH = 3;
	
	private static final byte ONE_BYTES_HEAD_LENGTH = 0x04;
	private static final byte FOUR_BYTES_HEAD_LENGTH = 0x07;
	private static final byte SIX_BYTES_HEAD_LENGTH = 0x09;
	private static final byte TWENTY_BYTES_HEAD_LENGTH = 0x17;
	private static final byte TIME_HEAD_LENGHT = 0x0A;
	
	private static final String NORTH = "N";
	private static final String SOUTH = "S";
	private static final String EAST = "E";
	private static final String WEST = "W";
	
	private DataBlockEncodeUtil(){}
	
	public static Map<DataBlockTypeEnum, Object> decodeDataBlock(byte[] dataBuff) throws IOException, DecoderException, ParseException{
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
					// byte
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
						
					// int
					case USER_ID:
					case WARN_INFO:
					case UNLOCK_TIME_DURATION:
						dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readIntReverse());
						break;
					
					// String
					case LOCK_MAC:
						stringBuf = new byte[size - HEAD_LENGTH];
						dataInputStream.read(stringBuf);
						dataBlockMap.put(dataBlockTypeEnum, Hex.encodeHexString(stringBuf));
						break;
					
					// String 去NUL
					case IMSI:
					case SOFTWARE_VERSION:
						stringBuf = new byte[size - HEAD_LENGTH];
						dataInputStream.read(stringBuf);
						String softwareVersion = new String(stringBuf);
						int zeroIndex = softwareVersion.indexOf('\0');
						dataBlockMap.put(dataBlockTypeEnum, zeroIndex == -1 ? softwareVersion : softwareVersion.substring(0, zeroIndex));
						break;
					
					// Date
					case SYS_TIME:
						stringBuf = new byte[size - HEAD_LENGTH];
						dataInputStream.read(stringBuf);
						dataBlockMap.put(dataBlockTypeEnum, UtilDateTime.convertCompactStringToTime(Hex.encodeHexString(stringBuf)));
						break;
						
					// Double
					case LOCK_LNG:
					case LOCK_LAT:
						stringBuf = new byte[size - HEAD_LENGTH];
						dataInputStream.read(stringBuf);
						String latLng = new String(stringBuf);
						if (latLng.charAt(0) == NORTH.charAt(0) || latLng.charAt(0) == EAST.charAt(0)) {
							latLng = latLng.replace(latLng.charAt(0), '+');
						}
						if (latLng.charAt(0) == SOUTH.charAt(0) || latLng.charAt(0) == WEST.charAt(0)) {
							latLng = latLng.replace(latLng.charAt(0), '-');
						}
						latLng = latLng.substring(0, latLng.indexOf('\0'));
						dataBlockMap.put(dataBlockTypeEnum, latLng.isEmpty() ? 0.0 : Double.valueOf(latLng));
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
		try(
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024); //包大小不会超过1k
			DataOutputStreamEx dataOutputStream = new DataOutputStreamEx(byteArrayOutputStream);
		){
		for(Entry<DataBlockTypeEnum, Object> entry: dataBlockMap.entrySet()){
			switch(entry.getKey()){
			// byte
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
				dataOutputStream.write(ONE_BYTES_HEAD_LENGTH);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.write((byte)dataBlockMap.get(entry.getKey()));
				break;
				
			// int
			case USER_ID:
			case WARN_INFO:
			case UNLOCK_TIME_DURATION:
				dataOutputStream.write(FOUR_BYTES_HEAD_LENGTH);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.writeIntReverse((int)dataBlockMap.get(entry.getKey()));
				break;
			
			// String
			case LOCK_MAC:
				String info = (String)dataBlockMap.get(entry.getKey());
				dataOutputStream.write(SIX_BYTES_HEAD_LENGTH);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.write(Hex.decodeHex(info.toCharArray()));
				break;
			
			// String 去NUL
			case IMSI:
			case SOFTWARE_VERSION:
				String softwareVersion = (String) dataBlockMap.get(entry.getKey());
				dataOutputStream.write(TWENTY_BYTES_HEAD_LENGTH);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.write(Arrays.copyOf(softwareVersion.getBytes(), 20));
				break;
			
			// Date
			case SYS_TIME:
				Date time = (Date)dataBlockMap.get(entry.getKey());
				dataOutputStream.write(TIME_HEAD_LENGHT);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.write(Hex.decodeHex(UtilDateTime.convertTimeToCompactString(time).toCharArray()));
				break;
			
			// Double
			case LOCK_LNG:
			case LOCK_LAT:
				Double d = (Double)dataBlockMap.get(entry.getKey());
				String latLng = null;
				if (entry.getKey() == DataBlockTypeEnum.LOCK_LAT) {
					latLng = d >= 0 ? (NORTH + d) : (SOUTH + d);
				}
				if (entry.getKey() == DataBlockTypeEnum.LOCK_LNG) {
					latLng = d >= 0 ? (EAST + d) : (WEST + d);
				}
				dataOutputStream.write(TWENTY_BYTES_HEAD_LENGTH);
				dataOutputStream.writeShortReverse(entry.getKey().toValue());
				dataOutputStream.write(Arrays.copyOf(latLng.getBytes(), 20));
				break;
			default:
				break;
			}
		}
		return byteArrayOutputStream.toByteArray();
	}
	}
}
