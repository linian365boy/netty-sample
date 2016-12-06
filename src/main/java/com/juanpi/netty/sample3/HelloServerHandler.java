package com.juanpi.netty.sample3;

import java.net.InetAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloServerHandler extends  SimpleChannelInboundHandler<String>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		//收到消息直接打印
		System.out.println(ctx.channel().remoteAddress() +" Say（说）:" +msg);
		//返回客户端消息
		//字符串最后面的"\n"是必须的。因为我们在前面的解码器DelimiterBasedFrameDecoder是一个根据字符串结尾为“\n”来结尾的。假如没有这个字符的话。解码会出现问题
		ctx.writeAndFlush("Server received you message!"+ctx.channel().remoteAddress()+"\n");
	}
	
	/**
	 * 在channel被启用的时候触发 (在建立连接的时候)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RemoteAddress:" +ctx.channel().remoteAddress()+" active!");
		//字符串最后面的"\n"是必须的。因为我们在前面的解码器DelimiterBasedFrameDecoder是一个根据字符串结尾为“\n”来结尾的。假如没有这个字符的话。解码会出现问题
		ctx.writeAndFlush("Welcome to "+InetAddress.getLocalHost().getHostName()+" service!\n");
		super.channelActive(ctx);
	}

}
