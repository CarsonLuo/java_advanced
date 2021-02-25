package io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioTimerClient {
    public static void main(String[] args) {
        new Thread(new NioClientHandle("localhost", 8080), "LYK_NioTimeClientThread-001").start();
    }
}

class NioClientHandle implements Runnable{
    private final String host;
    private final int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public NioClientHandle(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()){
                    SelectionKey key = it.next();
                    it.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        // 多路复用器关闭后, 所有注册在上面的channel 和 Pipe等资源都会被自动去注册并关闭, 所以不需要重复释放资源
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(!key.isValid()){
           return;
        }
        SocketChannel sc = (SocketChannel) key.channel();
        if(key.isConnectable()){
            // 判断是否连接成功
            if(sc.finishConnect()) {
                sc.register(selector, SelectionKey.OP_READ);
                doWrite(sc);
            }else {
                // 连接失败, 进程退出
                System.exit(1);
            }
        }
        if(key.isReadable()){
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = sc.read(readBuffer);
            if(readBytes > 0){
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("response : " + body);
                this.stop = true;
            }else if(readBytes < 0){
                // 对端链路关闭
                key.cancel();
                sc.close();
            }else {
                ; // 读到0字节, 忽略
            }
        }
    }

    private void doConnect() throws IOException {
        // 如果直接连接成功, 则注册到多路复用器上, 发送请求消息, 读应答
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else {
            // 如果没有直接连接成功, 说明服务器没有返回TCP握手应答消息, 不代表连接失败;
            // 当服务端返回TCP syn-ack消息后, 就能轮询到这个socketChannel处于 连接就绪状态
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req = "query time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        // 由于发送是异步的, 会存在 '半包写'的问题
        sc.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            // 缓存区的消息全部发送完成
            System.out.println("Send order to server success.");
        }
    }
}