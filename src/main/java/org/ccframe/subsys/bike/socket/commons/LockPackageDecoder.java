package org.ccframe.subsys.bike.socket.commons;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.CRC16Util;
import org.ccframe.subsys.bike.domain.code.BykeTypeCodeEnum;
import org.ccframe.subsys.bike.processor.NettyServerProcessor;
import org.ccframe.subsys.bike.socket.tcpobj.AnswerFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.ccframe.subsys.bike.socket.tcpobj.LockPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TCP解包类.
 * 传入 的内容是去掉标志位的数据，写回要带标志位
 * @author JIM
 *
 */
public class LockPackageDecoder extends SimpleChannelInboundHandler<byte[]> {

    protected Logger logger = LoggerFactory.getLogger(getClass()); //NOSONAR

	private static final int MIN_PACKAGE_SIZE = 1/*start*/ + 1/*procal type*/ + 1/*version*/ + 8/*lockid*/ + 1/*bike type*/ + 2/*command*/ + 2 /*CRC*/ + 1/*end*/;
	
	private static final byte BLE_GPRS_FLAG = 0x01;
	private static final byte BLE_FLAG = 0x02;
	private static final byte GPRS_FLAG = 0x03;
	
	ByteEscapeHelper byteEscapeHelper = new ByteEscapeHelper();
	
	public LockPackageDecoder(){
		//初始化转码功能
		byteEscapeHelper.addRule((byte)0x7E, new byte[]{0x5E, 0x7D});
		byteEscapeHelper.addRule((byte)0x5E, new byte[]{0x5E, 0x5D});
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SmartLockChannelUtil.unRegisterChannel(ctx.channel());
		System.out.println("channelInactive");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		if(msg.length < MIN_PACKAGE_SIZE){
			return;
		}

		//转义数据包：用 0x5E， 0x5D 来代替 0x5E；用 0x5E， 0x7D 来代替 0x7E。
		byte[] packageData = byteEscapeHelper.unescapeBytes(msg);
		
		//CRC16Util检测CRC是否正确
		int expectCrcValue = (short)(((packageData[packageData.length - 2]) & 0xff) + ((packageData[packageData.length - 1]) << 8));
		int crcValue = CRC16Util.crc16(packageData, packageData.length - 2);
		if(crcValue != expectCrcValue){
			logger.error("CRC Error! Expected: {}, CRC Value: {}", expectCrcValue, crcValue);
			return; //TODO FIX CRC错，要返回包请求重发
		}

		try (
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packageData);
			DataInputStreamEx dataInputStream = new DataInputStreamEx(byteArrayInputStream);

			ByteArrayOutputStream responseStream = new ByteArrayOutputStream(1024);
			DataOutputStreamEx responseDataOutputStream = new DataOutputStreamEx(responseStream);
		){
			LockPackage requestPackage = new LockPackage();
			requestPackage.setType(dataInputStream.readByte());
			requestPackage.setVersion(dataInputStream.readByte());
			requestPackage.setLockerHardwareCode(dataInputStream.readLongReverse());
			byte bykeType = dataInputStream.readByte();
			switch(bykeType){
				case BLE_GPRS_FLAG:
					requestPackage.setBykeTypeEnum(BykeTypeCodeEnum.BLE_GPRS);
					break;
				case BLE_FLAG:
					requestPackage.setBykeTypeEnum(BykeTypeCodeEnum.BLE);
					break;
				case GPRS_FLAG:
					requestPackage.setBykeTypeEnum(BykeTypeCodeEnum.GPRS);
					break;
				default:
			}
			//解析命令单元
			requestPackage.setCommandFlagEnum(CommandFlagEnum.fromValue(dataInputStream.read()));
			requestPackage.setAnswerFlagEnum(AnswerFlagEnum.fromValue(dataInputStream.read()));
			System.out.println("requestPack -> " + requestPackage);
			
			//解析数据单元
			byte[] dataBuff = new byte[dataInputStream.available() - 2];
			dataInputStream.read(dataBuff);
			Map<DataBlockTypeEnum, Object> dataBlock = DataBlockEncodeUtil.decodeDataBlock(dataBuff);
			requestPackage.setDataBlockMap(dataBlock);
			System.out.println("decode map  -> " + requestPackage.getDataBlockMap());
			
			//TODO 测试encode
//			byte[] encodeDataBlock = DataBlockEncodeUtil.encodeDataBlock(dataBlock);
//			System.out.println("encode      -> " + Hex.encodeHexString(encodeDataBlock));
//			System.out.println("encode map  -> " + DataBlockEncodeUtil.decodeDataBlock(encodeDataBlock));
			
			//检查连接，如果没有则放入到channel池
//			SmartLockChannelUtil.checkAndRegisterChannel(requestPackage.getLockerHardwareCode(), ctx.channel());

			Map<DataBlockTypeEnum, Object> responseDataMap = null;
			AnswerFlagEnum answerFlagEnum = AnswerFlagEnum.SUCCESS; //默认是成功
			boolean exceptionFlag = false;
			boolean commandNotSupportFlag = true;
			for(ISocketController socketController: SpringContextHelper.getBeansOfType(ISocketController.class).values()){
				if(socketController.getCommand() == requestPackage.getCommandFlagEnum()){
					commandNotSupportFlag = false;
					try{
						responseDataMap = socketController.execute(requestPackage.getLockerHardwareCode(), requestPackage.getDataBlockMap());
					}catch(Throwable tr){
						logger.error("socket executed with error!", tr);
						exceptionFlag = true;
					}
				}
			}
			if(commandNotSupportFlag){
				answerFlagEnum = AnswerFlagEnum.COMMAND_TYPE_ERROR;
			}else{
				if(exceptionFlag){
					answerFlagEnum = AnswerFlagEnum.OTHER_ERROR;
				}
			}

			//开始准备回写数据
			responseDataOutputStream.write(requestPackage.getType());
			responseDataOutputStream.write(requestPackage.getVersion());
			responseDataOutputStream.writeLong(requestPackage.getLockerHardwareCode());
			switch(requestPackage.getBykeTypeCodeEnum()){
				case BLE_GPRS:
					responseStream.write(BLE_GPRS_FLAG);
					break;
				case BLE:
					responseStream.write(BLE_FLAG);
					break;
				case GPRS:
					responseStream.write(GPRS_FLAG);
					break;
			}
			responseDataOutputStream.write(requestPackage.getCommandFlagEnum().toValue()); //应答包
			responseDataOutputStream.write(answerFlagEnum.toValue());
			if(answerFlagEnum == AnswerFlagEnum.SUCCESS && !responseDataMap.isEmpty()){ //如果成功就写数据块，否则出错就不用写
				responseDataOutputStream.write(DataBlockEncodeUtil.encodeDataBlock(responseDataMap));
			}
			int crcData = CRC16Util.crc16(responseStream.toByteArray());
			responseDataOutputStream.write(crcData >>> 8 & 0xff); //高位在先
			responseDataOutputStream.write(crcData >>> 0 & 0xff);
			
			//回写响应包
			ctx.channel().write(NettyServerProcessor.BOUND_DELIMITER.array());
			ctx.channel().write(responseStream.toByteArray());		
			ctx.channel().write(NettyServerProcessor.BOUND_DELIMITER.array());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}