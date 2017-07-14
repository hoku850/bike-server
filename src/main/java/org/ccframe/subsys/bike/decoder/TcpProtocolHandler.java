package org.ccframe.subsys.bike.decoder;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TcpProtocolHandler extends ByteToMessageDecoder {

	private static final byte BOUND_FLAG = 0X7E;
	private static final int MIN_PACKAGE_SIZE = 1/*start*/ + 1/*procal type*/ + 1/*version*/ + 8/*lockid*/ + 1/*bike type*/ + 2/*command*/ + 2 /*CRC*/ + 1/*end*/;  
	private byte data;
	boolean inbound; 

	private ByteArrayOutputStream frameStream = new ByteArrayOutputStream();
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//未达到最小解包条件
		if (in.readableBytes() < MIN_PACKAGE_SIZE) {
            return;
        }
		//判断有没有完整收到一个包，如果没有完整，则等待下一周期
		in.markReaderIndex(); //标记解包位置
		for(int i = 0; i < in.readableBytes(); i ++){
			data = in.readByte();
			if(data == BOUND_FLAG){
				if(inbound){
					if(frameStream.size() < MIN_PACKAGE_SIZE){ //丢弃帧
						frameStream.reset();
						return;
					}else{
						decodeFrame();
					}
				}else{
					inbound = true;
				}
			}else{
				frameStream.write(data);
			}
		}
		
		in.skipBytes("hello".length());
		in.markReaderIndex(); //标记解包位置，如果解包发现不完整（半包），需要继续等待剩余的数据到达后解包
		in.readBytes(System.out, in.readableBytes());
		System.out.print("\n");
		in.resetReaderIndex();
		//in.skipBytes(5);
	}

	private void decodeFrame(){
		
	}
	
//	@Override
//	protected void channelRead0(ChannelHandlerContext context, ByteBuf msg) throws Exception {
//		byte[] req = new byte[msg.readableBytes()];
//		msg.readBytes(req);
////		String body = new String(req, "UTF-8");
////		System.out.println("接收客户端数据：" + body);
//		System.out.println("接收客户端数据：" + Hex.encodeHexString(req).toUpperCase());
//		// 向客户端写数据
//		System.out.println("server开始向client发送数据");
//		String currentTime = new Date(System.currentTimeMillis()).toString();
//		ByteBuf resp = Unpooled.copiedBuffer("我草".getBytes());
//		// (重要) 一旦调用write方法，后续的所有handler都将不再执行
//		context.write(resp);
//	}

}
