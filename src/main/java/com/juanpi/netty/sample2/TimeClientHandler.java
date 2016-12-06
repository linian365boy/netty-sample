package com.juanpi.netty.sample2;

import java.util.Date;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @ClassName: EchoClientHandler  
 * @Description: @Sharable标记这个类的实例可以在 channel 里共享 
 * SimpleChannelInboundHandler 会在channelRead(会调channelRead0方法)方法之后自动释放对 ByteBuf（保存信息） 的引用
 * @date: 2016年12月2日 上午10:10:35 
 * 
 * @author tanfan 
 * @version  
 * @since JDK 1.7
 */
public class TimeClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf) msg;
		try{
			long currentTimeMills = (m.readUnsignedInt() - 2208988800L) * 1000L;
			System.out.println("client read data "+new Date(currentTimeMills));
			channelRead0(ctx,m);
			ctx.close();
		}finally{
			m.release();
		}
	}
	
	/**
	 * 和服务器的连接建立起来后被调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8));
	}
	
	/**
	 * 从服务器收到一条消息时被调用
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		System.out.println("Client received: "+msg.toString(CharsetUtil.UTF_8));
	}
	
	/**
	 * 处理过程中异常发生时被调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
