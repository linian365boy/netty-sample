package com.juanpi.netty.sample1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @ClassName: EchoServerHandler  
 * @Description: @Sharable标识这类的实例之间可以在 channel 里面共享 
 * @date: 2016年12月2日 上午9:56:18 
 * 
 * @author tanfan 
 * @version  
 * @since JDK 1.7
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
	/**
	 * 每次收到消息时被调用
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf)msg;
		System.out.println("Server received : "+ in.toString(CharsetUtil.UTF_8));
		ctx.write(in);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Server accept active connect");
	}
	
	/**
	 * 通知处理器最后的 channelread() 是当前批处理中的最后一条消息时调用
	 * 因channelRead的ctx.write() 是异步的。
	 * 在 channelRead()返回时，可能还没有完成。
	 * 最后在 channelReadComplete() 我们调用 ctxWriteAndFlush() 来释放信息
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * 在读操作异常被抛出时被调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
