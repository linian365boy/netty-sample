package com.juanpi.netty.sample7;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class FactorialClientHandler extends SimpleChannelInboundHandler<BigInteger> {
	private ChannelHandlerContext ctx;
    private int receivedMessages;
    private int next = 1;
    final BlockingQueue<BigInteger> answer = new LinkedBlockingQueue<BigInteger>();

    public BigInteger getFactorial() {
        boolean interrupted = false;
        try {
            for (;;) {
                try {
                	//取出队列中的头
                	BigInteger tempVal = answer.take();
                	System.out.println("getFactorial tempVal的值 ："+tempVal);
                    return tempVal;
                } catch (InterruptedException ignore) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        sendNumbers();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, final BigInteger msg) {
        receivedMessages ++;
        System.out.println("receivedMessages的值："+receivedMessages+" msg的值："+msg);
        if (receivedMessages == FactorialClient.COUNT) {
        	System.out.println("进入最后的阶段了");
            // Offer the answer after closing the connection.
            ctx.channel().close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                	//Inserts the specified element into this queue
                    boolean offered = answer.offer(msg);
                    assert offered;
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendNumbers() {
        // Do not send more than 4096 numbers.
        ChannelFuture future = null;
        for (int i = 0; i < 4096 && next <= FactorialClient.COUNT; i++) {
        	System.out.println("next的值为："+next);
            future = ctx.write(Integer.valueOf(next));
            next++;
        }
        System.out.println("next循环后的最终值为："+next+" count值"+FactorialClient.COUNT);
        if (next <= FactorialClient.COUNT) {
        	System.out.println("numberSender添加监听器1");
            assert future != null;
            System.out.println("numberSender添加监听器2");
            future.addListener(numberSender);
        }
        ctx.flush();
    }

    private final ChannelFutureListener numberSender = new ChannelFutureListener() {
       @Override
       public void operationComplete(ChannelFuture future) throws Exception {
    	   System.out.println("监听器内部调用operationComplete");
           if (future.isSuccess()) {
        	   System.out.println("ctx写入成功，重新调用sendNumbers");
               sendNumbers();
           } else {
               future.cause().printStackTrace();
               future.channel().close();
           }
       }
   };
}
