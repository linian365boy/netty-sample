package com.juanpi.netty.sample2;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ClassName: EchoClient  
 * @Description: 
 * 步骤：<br/>
 *  1、创建一个Bootstrap实例来初始化客户端<br/>
	2、分配一个NioEventLoopgroup实例来处理事件，包括创建新的连接和处理输入输出数据<br/>
	3、创建一个InetSocketAddress用于连接到服务器<br/>
	4、当连接建立，一个EchoClientHandler会被装入pipeline<br/>
	5、所有东西都创建完毕后，调用Bootstrap.connet()连接到远端。 <br/>
 * @date: 2016年11月28日 下午6:07:46 
 * 
 * @author tanfan 
 * @version  
 * @since JDK 1.7
 */
public class TimeClient {
	private final String host;
	private final int port;
	
	public TimeClient(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 8080;
		if(args.length == 2){
			//System.err.println("Usage: "+TimeClient.class.getSimpleName()+"<host> <port>");
			//return;
			host = args[0];
			port = Integer.parseInt(args[1]);
		}
		new TimeClient(host,port).start();
	}

	private void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true);
			b.remoteAddress(new InetSocketAddress(host, port))
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new TimeClientHandler());
					}
				});
			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully().sync();
		}
	}
}
