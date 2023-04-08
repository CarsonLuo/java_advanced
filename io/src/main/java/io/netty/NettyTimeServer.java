package io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class NettyTimeServer {

    public static void main(String[] args) throws Exception {
        new NettyTimeServer().bind(8080);
    }

    public void bind(int port) throws InterruptedException {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 负责接收
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 负责处理网络读写(I/O)
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 配置 TCP 参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 绑定IO时间的处理类
                .childHandler(new ChildChannelHandler());

        try {
            // 绑定端口, 同步等待成功
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            // 等待服务端监听端口关闭(阻塞) [阻塞完毕后, main函数退出]
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅退出, 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new TimeServerHandler());
    }
}

class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("netty server accept new client");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 做类型转换 将msg转化为netty的ByteBuf对象
        ByteBuf buf = (ByteBuf) msg;

        // 获取缓冲区可读的字节数 根据可读字节数创建byte数组
        byte[] req = new byte[buf.readableBytes()];

        // 将缓冲区的字节数复制到新建的byte数组
        buf.readBytes(req);

        // 对请求消息进行判断 如果是"QUERY TIME" 则创建应答消息  通过ChannelHandlerContext的write方法异步发送应答消息给客户端
        // netty 的 write 方法并不直接将消息写入 SocketChannel中,
        // 调用write方法只是把待发送的消息放到发送缓冲数组中
        // 再通过调用flush方法, 将发送缓冲区中的消息全部写到 SocketChannel 中
        String body = new String(req, StandardCharsets.UTF_8);
        System.out.println("The time server receive order: " + body);
        String currentTime = "query time".equalsIgnoreCase(body)?new Date(
                System.currentTimeMillis()).toString():"BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*
         * 将消息发送队列中的消息写入到SocketChannel中发送给对方  通过调用write方法将消息发送到缓冲数组中 再调用flush方法将
         * 缓冲区的消息全部写入SocketChannel
         */
        System.out.println("channel read complete.");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("netty time error, exceptionCaught.");
        cause.printStackTrace();
        // 关闭, 释放与 ChannelHandlerContext 相关联的句柄等资源
        ctx.close();
    }
}