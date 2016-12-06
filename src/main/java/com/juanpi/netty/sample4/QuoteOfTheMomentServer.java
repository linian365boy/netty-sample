package com.juanpi.netty.sample4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @ClassName: QuoteOfTheMomentServer  
 * @Description: 广播，服务器随机产生一句名言发送到客户端 
 * @date: 2016年12月6日 上午10:39:15 
 * 
 * @author tanfan 
 * @version  
 * @since JDK 1.7
 */
public class QuoteOfTheMomentServer {
	private static final int PORT = Integer.parseInt(System.getProperty("port","8082"));
	public static void main(String[] args) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			 .channel(NioDatagramChannel.class)
			 .option(ChannelOption.SO_BROADCAST, true)
			 .handler(new QuoteOfTheMomentServerHandler());
			b.bind(PORT).sync().channel().closeFuture().await();
		}finally{
			group.shutdownGracefully();
		}
	}
}
