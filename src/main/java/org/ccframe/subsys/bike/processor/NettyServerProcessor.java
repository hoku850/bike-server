package org.ccframe.subsys.bike.processor;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.ccframe.commons.base.IProcesser;
import org.ccframe.commons.helper.SysInitBeanHelper;
import org.ccframe.subsys.bike.decoder.LockPackageDecoder;
import org.ccframe.subsys.bike.decoder.TcpProtocolHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;

@Component
public class NettyServerProcessor implements IProcesser{
	
	private Logger log = Logger.getLogger(this.getClass());

	private NioEventLoopGroup bossGroup = new NioEventLoopGroup();
	private NioEventLoopGroup workerGroup = new NioEventLoopGroup();

	private static final ByteBuf BOUND_DELIMITER = Unpooled.copiedBuffer(new byte[]{0X7E});
	
	private int port;

	@Value("${app.netty.port:8081}")
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	@Scheduled(fixedDelay=2*1000)
	public void process() {
		if(!SysInitBeanHelper.inited){
			return;
		}

		log.info("在端口"+port+"启动netty监听...");
		try {
			ServerBootstrap b = new ServerBootstrap();
			b = b.group(bossGroup, workerGroup);
			b = b.channel(NioServerSocketChannel.class);

			b = b.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					.addLast(new DelimiterBasedFrameDecoder(1024, BOUND_DELIMITER)) //发送和接收的数据包总长度（包括头尾的 0X7E）不超过 1024 个字节
					.addLast(new ByteArrayDecoder())
					.addLast(new ByteArrayEncoder())
					.addLast(new LockPackageDecoder());
				}
			});
			b = b.option(ChannelOption.SO_BACKLOG, 128);
			b = b.childOption(ChannelOption.SO_KEEPALIVE, true); //保持长连接状态
			//通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
			b.option(ChannelOption.TCP_NODELAY, true);
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	@PreDestroy
	public void exit() {
		log.info("退出netty...");
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}
}
