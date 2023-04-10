package io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 对于 AsynchronousServerSocketChannel , AsynchronousSocketChannel
 * 它们都是由JDK底层的线程池负责回调并驱动读写操作的.
 */
public class AioTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new AsyncTimeServerHandler(port), "lyk-aioTimeServerThread-001").start();
    }
}

class AsyncTimeServerHandler implements Runnable{
    CountDownLatch latch;
    AsynchronousServerSocketChannel serverChannel;

    public AsyncTimeServerHandler(int port){
        try {
            serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            System.out.println("aio time server start, port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doAccept(){
        serverChannel.accept(this, new AcceptCompletionHandler());
    }
}

class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler>{
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        // 这里继续accept是因为: 等待下一个连接; 总体形成一个循环;
        attachment.serverChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        attachment.latch.countDown();
    }
}

class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer>{
    private final AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        String req = new String(body, StandardCharsets.UTF_8);
        System.out.println("aio time server accept request : " + req);
        String currentTimeStr = req.equalsIgnoreCase("query time") ?
                new Date(System.currentTimeMillis()).toString() : "bad order";
        doWrite(currentTimeStr);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String response){
        if(response == null || response.trim().length() <= 0){
            return;
        }
        byte[] bytes = response.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer, writeBuffer,
                new CompletionHandler<>() {
                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        if (buffer.hasRemaining()) {
                            channel.write(buffer, buffer, this);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer buffer) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                });
    }
}
