package com.juanpi.netty.sample4;

import java.util.Random;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class QuoteOfTheMomentServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private static final Random random = new Random();
	
	//Quotes from Mohands K. Gandhi:
	private static final String[] quotes = {
			"Where there is love there is life.",
			"First they ignore you, then they laugh at you, then they fight you, then you win.",
			"Be the change you want to see in the world.",
			"The weak can never forgive. Forgiveness is the attribute of the strong."
	};
	
	private static String nextQuoto(){
		int quoteId;
		synchronized (random) {
			quoteId = random.nextInt(quotes.length);
		}
		return quotes[quoteId];
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		System.err.println(packet);
		if("QOTM?".equals(packet.content().toString(CharsetUtil.UTF_8))){
			ctx.write(new DatagramPacket(Unpooled.copiedBuffer("QOTM: "+nextQuoto(), CharsetUtil.UTF_8), packet.sender()));
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
