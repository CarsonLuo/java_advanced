package io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class AioTimeClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        new Thread(new AsyncTimeClientHandler(host,port), "lyk-aioTimeServerThread-001").start();
    }
}

class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable{
    private AsynchronousSocketChannel client;
    private final String host;
    private final int port;
    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host,port), this, this);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        byte[] req = "query time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, ByteBuffer writeBuffer) {
                if(writeBuffer.hasRemaining()){
                    client.write(writeBuffer, writeBuffer, this);
                }else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer readBuffer) {
                            readBuffer.flip();
                            byte[] bytes = new byte[readBuffer.remaining()];
                            readBuffer.get(bytes);
                            String body;
                            body = new String(bytes, StandardCharsets.UTF_8);
                            System.out.println("now is : " + body);
                            latch.countDown();
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer readBuffer) {
                            try {
                                client.close();
                                latch.countDown();
                            } catch (IOException e) {
                                //
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                try {
                    client.close();
                    latch.countDown();
                } catch (IOException e) {
                    //
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        try {
            client.close();
            latch.countDown();
        } catch (IOException e) {
            //
        }
    }
}