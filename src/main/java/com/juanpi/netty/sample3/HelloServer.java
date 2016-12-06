package com.juanpi.netty.sample3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
	private static int port = 8081;
	private static String host = "127.0.0.1";
	
	public static void main(String[] args) throws Exception {
		EventLoopGroup boosGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(boosGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(host, port)
				.childHandler(new HelloServerInitializer());
			// 服务器绑定端口监听
			ChannelFuture f = b.bind().sync();
			// 监听服务器关闭监听
			f.channel().closeFuture().sync();
		}finally{
			boosGroup.shutdownGracefully().sync();
			workerGroup.shutdownGracefully().sync();
		}
	}
}
