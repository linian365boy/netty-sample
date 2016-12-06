package com.juanpi.netty.sample3;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloClientInitializer extends ChannelInitializer<SocketChannel>{
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	private static final HelloServerHandler SERVER_HANDLER = new HelloServerHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipline = ch.pipeline();
		//以\r \n 为结尾的解码器
		//这个地方的 必须和服务端对应上。否则无法正常解码和编码
		pipline.addLast("frame", new DelimiterBasedFrameDecoder(8*1024, Delimiters.lineDelimiter()));
		//字符串解码 和 编码
		pipline.addLast("decoder", DECODER);
		pipline.addLast("encoder", ENCODER);
		//自己的逻辑
		pipline.addLast("handler", SERVER_HANDLER);
	}
}
