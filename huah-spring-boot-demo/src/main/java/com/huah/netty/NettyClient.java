package com.huah.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author huah 2023/05/17 16:09
 */
public class NettyClient {
    public static void main(String[] args) {
        //构造两个线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //服务端启动辅助类
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush("hello, 服务端");
                                }
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("客户端收到消息：" + msg);
                                    Thread.sleep(2000);
                                    ctx.writeAndFlush("客户端已收到消息: 你好服务端");
                                }
                            });
                        }
                    });

            ChannelFuture future = bootstrap.connect("127.0.0.1",8080);
            //等待服务端口关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 优雅退出，释放线程池资源
            workerGroup.shutdownGracefully();
        }
    }
}
