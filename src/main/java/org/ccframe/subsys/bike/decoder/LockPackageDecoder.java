package org.ccframe.subsys.bike.decoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import org.apache.commons.codec.binary.Hex;
import org.ccframe.subsys.bike.domain.code.BykeTypeCodeEnum;
import org.ccframe.subsys.bike.tcpobj.AnswerFlagEnum;
import org.ccframe.subsys.bike.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.tcpobj.DataBlockTypeEnum;
import org.ccframe.subsys.bike.tcpobj.LockPackage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
//import sun.misc.CRC16;

public class LockPackageDecoder extends SimpleChannelInboundHandler<byte[]> {

	private static final int MIN_PACKAGE_SIZE = 1/*start*/ + 1/*procal type*/ + 1/*version*/ + 8/*lockid*/ + 1/*bike type*/ + 2/*command*/ + 2 /*CRC*/ + 1/*end*/;  

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		if(msg.length < MIN_PACKAGE_SIZE){
			return;
		}

//		//解析CRC
//		CRC16 crc = new CRC16();
//		for(int i = 1; i < msg.length - 4; i ++){
//			crc.update(msg[i]);
//		}
//		System.out.println("crc=" + Long.toHexString(crc.value));
//		int crcValue = ((int)msg[msg.length - 2] | msg[msg.length - 1] << 8);
//		System.out.println("crcValue=" + Integer.toHexString(crcValue).substring(4));
//		if(crc.value != crcValue){
//			//JAVA的CRC没有一个版本对得上的
//		}
		
		try (
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(msg);
			DataInputStreamEx dataInputStream = new DataInputStreamEx(byteArrayInputStream);
		){
			LockPackage lockPackage = new LockPackage();
			lockPackage.setType(dataInputStream.readByte());
			lockPackage.setVersion(dataInputStream.readByte());
			lockPackage.setLockId(dataInputStream.readLongReverse());
			byte bykeType = dataInputStream.readByte();
			switch(bykeType){
				case 0x01:
					lockPackage.setBykeTypeEnum(BykeTypeCodeEnum.BLE_GPRS);
					break;
				case 0x02:
					lockPackage.setBykeTypeEnum(BykeTypeCodeEnum.BLE);
					break;
				case 0x03:
					lockPackage.setBykeTypeEnum(BykeTypeCodeEnum.GPRS);
					break;
				default:
			}
			//解析命令单元
			lockPackage.setCommandFlagEnum(CommandFlagEnum.fromValue(dataInputStream.readByte()));
			lockPackage.setAnswerFlagEnum(AnswerFlagEnum.fromValue(dataInputStream.readByte()));
			
			//解析数据单元
			byte[] dataBuff = new byte[dataInputStream.available() - 2];
			dataInputStream.read(dataBuff);
			
			lockPackage.setDataBlockMap(decodeDataBlock(dataBuff));
			
			System.out.println(lockPackage.getDataBlockMap());
		}
	}
	
	private Map<DataBlockTypeEnum, Object> decodeDataBlock(byte[] dataBuff) throws IOException{
		Map<DataBlockTypeEnum, Object> dataBlockMap = new HashMap<>();
		try (
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataBuff);
			DataInputStreamEx dataInputStream = new DataInputStreamEx(byteArrayInputStream);
		){
			while(dataInputStream.available() > 0){
				try{
					int size = dataInputStream.readByte();
					short dataBlockType = dataInputStream.readShortReverse();
					DataBlockTypeEnum dataBlockTypeEnum = DataBlockTypeEnum.fromValue(dataBlockType);
					switch(dataBlockTypeEnum){
						case LOCK_STATUS:
						case LOCK_BATTERY:
						case GPRS_KEEP_ALIVE:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case USER_ID:
						case UNLOCK_TIME_DURATION:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readIntReverse());
						case LOCK_LNG:
						case LOCK_LAT:
							byte[] stringBuf = new byte[size - 3];
							dataInputStream.read(stringBuf);
							dataBlockMap.put(dataBlockTypeEnum, new String(stringBuf, "GBK"));
					}
				}catch(ArrayIndexOutOfBoundsException e){
					break;
				}
			}
		}
		return dataBlockMap;
	}

}
