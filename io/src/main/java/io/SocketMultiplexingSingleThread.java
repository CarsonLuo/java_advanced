package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用单线程
 */
public class SocketMultiplexingSingleThread {

    private ServerSocketChannel server = null;
    private Selector selector = null;
    int port = 9090;

    public void initServer(){
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        initServer();
        System.out.println("初始化服务器...., 服务器初始化完毕!");
        try {
            while (true){
                while ((selector.select(0) > 0)){ //询问内核是否有 '事件'
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();  // 从 '多路复用器' 取出 '有效' 的key
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()){
                            acceptHandle(key);
                        }else if(key.isReadable()){
                            readHandle(key);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptHandle(SelectionKey selectionKey){
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            client.register(selector, SelectionKey.OP_READ, byteBuffer);
            System.out.println("--------------------------------------------------");
            System.out.println("新客户端: " + client.getRemoteAddress());
            System.out.println("--------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHandle(SelectionKey selectionKey){
        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        byteBuffer.clear();
        try {
            while (true) {
                int read = client.read(byteBuffer);
                if (read > 0) {
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()){
                        client.write(byteBuffer);
                    }
                    byteBuffer.clear();
                }else if(read == 0){
                    break;
                }else {
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketMultiplexingSingleThread singleThread = new SocketMultiplexingSingleThread();
        singleThread.start();
    }
}
