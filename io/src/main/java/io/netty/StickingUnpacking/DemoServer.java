package io.netty.StickingUnpacking;

import io.netty.NettyTimeServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * TCP 粘包/拆包发生的原因
 * (1) 应用程序 write 写入的字节大小 大于 'socket发送缓冲区'的大小
 * (2) 进行 MSS 大小的 TCP分段
 * (3) 以太网帧的 payload 大于 MTU 进行 IP 分片
 *
 * 业界主流协议的解决方式
 * (1) 消息定长, 例如每个报文固定长度 200字节, 如果不够, 空位补充空格
 * (2) 包位 添加 '回城换行符' 进行分割, 例如 FTP;
 * (3) 消息分为 '消息头' 和 '消息体', 消息头包含表示 '消息总长度'的字段 (例如用 int32表示)
 * (4) 更复杂的应用层协议
 *
 * LineBasedFrameDecoder : 遍历 ByteBuf 中的可读字符, 如果有 '\n' 或 '\r\n', 就以此为 '结束位置', [可读索引 -> 结束位置] 组成一行
 * 支持'携带'或'不携带'结束符两种解码方式; 支持配置 '单行的最大长度'.
 * 如果连续读取到最大长度后仍然没发现换行符, 就会抛出异常, 同时忽略掉之前读到的异常码流;
 *
 * StringDecoder : 将接受的对象转换成字符串
 *
 * LineBasedFrameDecoder + StringDecoder : 按行切换的文本解码器
 */
public class DemoServer {
    public static void main(String[] args) throws Exception {
        new DemoServer().bind(8080);
    }

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 负责接收
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 负责处理网络读写(I/O)
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder()) //
                                .addLast(new ServerHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅退出, 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class ServerHandler extends ChannelInboundHandlerAdapter {
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // ByteBuf buf = (ByteBuf) msg;
        // byte[] req = new byte[buf.readableBytes()];
        // buf.readBytes(req);
        // String body = new String(req, StandardCharsets.UTF_8).substring(0,req.length - System.getProperty("line.separator").length());

        String body = (String)msg;
        System.out.println("The time server receive order: " + body
                + ", and count : " + ++count);

        String currentTime = "query time".equalsIgnoreCase(body) ?
                new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("netty time error, exceptionCaught.");
        cause.printStackTrace();
        ctx.close();
    }
}