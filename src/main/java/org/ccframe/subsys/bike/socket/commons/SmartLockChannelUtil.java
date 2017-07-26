package org.ccframe.subsys.bike.socket.commons;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.ccframe.commons.util.CRC16Util;
import org.ccframe.commons.util.UtilDateTime;
import org.ccframe.subsys.bike.processor.NettyServerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBiMap;

import io.netty.channel.Channel;

/**
 * netty长连接反向推送工具.
 * @author JIM
 *
 */
public class SmartLockChannelUtil {
	
	private static final int MAX_CONNECTION=10000; /* 默认10000个长连接 */
	
	private static final int READ_TIMEOUT_MILLIS=15*1000; /* 15秒内将数据发出去 */

	private static final int UNLOCKING_SIZE = 1000;
	
	private static LinkedHashMap<Long, Boolean> openMessageMap = new LinkedHashMap<Long, Boolean>(){ //开锁消息队列

		private static final long serialVersionUID = -657968168039482524L;

		protected boolean removeEldestEntry(Map.Entry<Long, Boolean> eldest) {
		    // 当前记录数大于设置的最大的记录数，删除最旧记录（即最近访问最少的记录）
		    return size() > UNLOCKING_SIZE;
		}
	};
	
	private static ByteEscapeHelper byteEscapeHelper = new ByteEscapeHelper();
	{
		//初始化转码功能
		byteEscapeHelper.addRule((byte)0x7E, new byte[]{0x5E, 0x7D});
		byteEscapeHelper.addRule((byte)0x5E, new byte[]{0x5E, 0x5D});
	}

	
	/**
	 * 双向map
	 */
	private static HashBiMap<Long,Channel> chanelMap = HashBiMap.<Long,Channel>create(MAX_CONNECTION);
	
	private static Logger logger = LoggerFactory.getLogger(SmartLockChannelUtil.class);
	
	synchronized public static void checkAndRegisterChannel(Long lockerHardwareCode, Channel channel){
		if(chanelMap.get(lockerHardwareCode) == null){
			chanelMap.put(lockerHardwareCode, channel);
			logger.info("connected smartLockId channel >> {}", lockerHardwareCode);
		}
	}

	synchronized public static void unRegisterChannel(Channel channel){
		logger.info("remove smartLockId >> {}", chanelMap.inverse().remove(channel));
	}
	
	public static boolean isChannelActive(Long lockerHardwareCode){
		return chanelMap.get(lockerHardwareCode) != null;
	}
	
	public static boolean tryUnlock(Long lockerHardwareCode){
		Channel channel = chanelMap.get(lockerHardwareCode);
		if(channel != null){
			//开锁请求 , byte[] bytes、、TODO
			try (
				ByteArrayOutputStream responseStream = new ByteArrayOutputStream(1024);
				DataOutputStreamEx responseDataOutputStream = new DataOutputStreamEx(responseStream);
			){
				responseDataOutputStream.write(0x01); //协议类型
				responseDataOutputStream.write(0x01); //协议版本号
				responseDataOutputStream.writeLongReverse(lockerHardwareCode); //单车设备号
				responseDataOutputStream.write(0x01); //单车类别
				responseDataOutputStream.write(0x04); //命令字：开锁
				responseDataOutputStream.write(0xFF); //纯命令
				
				responseDataOutputStream.write(0x04); //数据包长
				responseDataOutputStream.writeShortReverse(0x0104); //锁开关
				responseDataOutputStream.write(0x01); //打开锁
				
				responseDataOutputStream.write(0x07); //数据包长
				responseDataOutputStream.writeShortReverse(0x0052); //用户ID
				responseDataOutputStream.writeIntReverse(0); //用户ID值
				
				responseDataOutputStream.write(0x0a); //数据包长
				responseDataOutputStream.writeShortReverse(0x0150); //时间
				responseDataOutputStream.write(Hex.decodeHex(UtilDateTime.convertTimeToCompactString(new Date()).toCharArray())); //BCD时间
				responseDataOutputStream.writeShortReverse(CRC16Util.crc16(responseStream.toByteArray()));
				
				channel.write(NettyServerProcessor.BOUND_DELIMITER.array());
				channel.write(byteEscapeHelper.escapeBytes(responseStream.toByteArray()));
				channel.writeAndFlush(NettyServerProcessor.BOUND_DELIMITER.array());

				synchronized(channel){
					Boolean locked = openMessageMap.get(lockerHardwareCode);
					if(locked == null){ //开锁完毕
						openMessageMap.put(lockerHardwareCode, false); //将打开超时也标记为OTHER_ERROR，biaoshi
						channel.wait(READ_TIMEOUT_MILLIS); //等开锁回复命令notifyall
					}
					return openMessageMap.remove(lockerHardwareCode); //开锁成功返回true，失败或READ_TIMEOUT_MILLIS超时返回false
				}
			} catch (Throwable tr) {
				logger.error("", tr);
				return false;
			}
		}
		return false;
	}
	
	public static void notifyUnlocked(Long lockerHardwareCode){
		Channel channel = chanelMap.get(lockerHardwareCode);
		if(channel != null && openMessageMap.get(lockerHardwareCode) != null){
			openMessageMap.put(lockerHardwareCode, true);
			synchronized(channel){
				channel.notifyAll(); //通知开锁线程继续
			}
		}
	}
}
