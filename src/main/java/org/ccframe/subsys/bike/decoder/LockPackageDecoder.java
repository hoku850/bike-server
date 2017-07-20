package org.ccframe.subsys.bike.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
//import sun.misc.CRC16;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ccframe.subsys.bike.domain.code.BykeTypeCodeEnum;
import org.ccframe.subsys.bike.tcpobj.AnswerFlagEnum;
import org.ccframe.subsys.bike.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.tcpobj.DataBlockTypeEnum;
import org.ccframe.subsys.bike.tcpobj.LockPackage;

public class LockPackageDecoder extends SimpleChannelInboundHandler<byte[]> {

	private static final int MIN_PACKAGE_SIZE = 1/*start*/ + 1/*procal type*/ + 1/*version*/ + 8/*lockid*/ + 1/*bike type*/ + 2/*command*/ + 2 /*CRC*/ + 1/*end*/;  

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		SmartLockChannelUtil.registerChannel(34234, ctx.channel());
		System.out.println("channelActive");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		System.out.println("channelUnregistered");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println("channelInactive");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent)evt;
			System.out.println("State:" + event.state());
		}
		System.out.println("userEventTriggered");
	}

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
			
			System.out.println("pack -> " + lockPackage);
			
			//解析数据单元
			byte[] dataBuff = new byte[dataInputStream.available() - 2];
			dataInputStream.read(dataBuff);
			lockPackage.setDataBlockMap(decodeDataBlock(dataBuff));
			System.out.println("map -> " + lockPackage.getDataBlockMap());
			
			// 解析CRC
			short CRC = dataInputStream.readShortReverse();
			System.out.println("CRC -> " + CRC);

			ctx.channel().writeAndFlush("this is return packet".getBytes());
			System.out.println("writed -> ");
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
					byte[] stringBuf;
					switch(dataBlockTypeEnum){
						case LOCK_STATUS:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case LOCK_BATTERY:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case LOCK_MAC:
							stringBuf = new byte[size - 3];
							dataInputStream.read(stringBuf);
							//dataBlockMap.put(dataBlockTypeEnum, new String(stringBuf, "utf-8"));
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.getStr(stringBuf));
							break;
						case SYS_TIME:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readTimeReverse());
							break;
						case GPS_INFO:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case SOFTWARE_VERSION:
							stringBuf = new byte[size - 3];
							dataInputStream.read(stringBuf);
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.getStr(stringBuf));
							break;
						
						case IMSI:
							stringBuf = new byte[size - 3];
							dataInputStream.read(stringBuf);
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.getStr(stringBuf));
							break;
						case CSQ:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
							
						case LOCK_IF_OPEN:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case USER_ID:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readIntReverse());
							break;
						case LOCK_ERROR:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						
						case GPRS_KEEP_ALIVE:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case UNLOCK_TIME_DURATION:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readIntReverse());
							break;
						case LOCK_LNG:
							stringBuf = new byte[size - 3];
							dataInputStream.read(stringBuf);
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.getStr(stringBuf));
							break;
						case LOCK_LAT:
							stringBuf = new byte[size - 3];
							dataInputStream.read(stringBuf);
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.getStr(stringBuf));
							break;
						case CPRS_SATELLITE_NUM:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case GPS_REPORT_TYPE:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
							break;
						case WARN_INFO:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readIntReverse());
							break;
						case UPGRADE:
							dataBlockMap.put(dataBlockTypeEnum, dataInputStream.readByte());
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
	
//	private byte[] encodeDataBlock(Map<DataBlockTypeEnum, Object> dataBlockMap) {
//		try (
//			ByteArrayOutputStream output = new ByteArrayOutputStream();
//			ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
//		){
//			objectOutputStream.writeObject(dataBlockMap);
//			objectOutputStream.flush();
//		} catch (Exception e) {
//			
//		}
//		return output.toByteArray();
//	}

}
