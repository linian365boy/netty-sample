package com.juanpi.netty.sample2;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @ClassName: EchoServer  
 * @Description: 
 * 步骤：<br/>
 *  1、创建一个ServerBootstrap实例来启动和绑定服务器<br/>
	2、创建并且分配一个NioEventLoopgroup实例来处理event，比如接受新的连接和读/写数据<br/>
	3、指定本地InetSocketAddress到服务器绑定的端口<br/>
	4、用一个EchoServerHandler实例来初始化每个新的Channel<br/>
	5、调用ServerBootstrap.bind()来绑定服务器。 <br/>
 * @date: 2016年11月28日 下午6:09:12 
 * 
 * @author tanfan 
 * @version  
 * @since JDK 1.7
 */
public class TimeServer {
	private final int port;
	public TimeServer(int port){
		this.port = port;
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8080;
		//if(args.length!=1){
		//	System.err.println("Usage:"+EchoServer.class.getSimpleName()+"<port>");
		//	return;
		//}
		if(args.length==1){
			port = Integer.parseInt(args[0]);
		}
		new TimeServer(port).start();
	}

	private void start() throws Exception {
		final TimeServerHandler serverHandler = new TimeServerHandler();
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(serverHandler);
					}
				});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully().sync();
		}
	}
}
