package com.juanpi.netty.sample3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HelloClient {
	private static int port = 8081;
	private static String host = "127.0.0.1";
	public static void main(String[] args) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.remoteAddress(new InetSocketAddress(host, port))
				.handler(new HelloClientInitializer());
			//连接服务端
			Channel ch = b.connect().sync().channel();
			//控制台输入
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				String line = in.readLine();
				if(line == null){
					continue;
				}
				/**
				 * 向服务器发送在控制台输入的文本，并用"\n"结尾
				 */
				ch.writeAndFlush(line+"\n");
			}
		}finally{
			group.shutdownGracefully().sync();
		}
	}
}
