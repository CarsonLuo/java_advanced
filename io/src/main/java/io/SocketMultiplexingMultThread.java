package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多路复用, 多线程
 */
public class SocketMultiplexingMultThread {

    private ServerSocketChannel server = null;
    private Selector bossSelector = null;
    private Selector workerSelector1 = null;
    private Selector workerSelector2 = null;
    int port = 9090;

    public void initServer(){
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            bossSelector = Selector.open();
            workerSelector1 = Selector.open();
            workerSelector2 = Selector.open();
            server.register(bossSelector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SocketMultiplexingMultThread multThread = new SocketMultiplexingMultThread();
        multThread.initServer();

        NioThread bossThread = new NioThread(multThread.bossSelector, 2);
        NioThread workerThread1 = new NioThread(multThread.workerSelector1);
        NioThread workerThread2 = new NioThread(multThread.workerSelector2);

        bossThread.start();

        Thread.sleep(1000);
        workerThread1.start();
        workerThread2.start();

        System.out.println("服务器启动完成!");
    }
}

class NioThread extends Thread{
    static LinkedBlockingDeque<SocketChannel>[] queue;
    static AtomicInteger idx= new AtomicInteger();
    static int workerCount;

    private int id;
    private Selector selector;
    private boolean boss;

    public NioThread(Selector selector, int workerCount){
        this.selector = selector;
        NioThread.workerCount = workerCount;
        this.boss = true;

        queue = new LinkedBlockingDeque[workerCount];
        for(int i = 0; i < workerCount; i++){
            queue[i] = new LinkedBlockingDeque<>();
        }
        System.out.println("boss 启动完成!");
    }

    public NioThread(Selector selector){
        this.selector = selector;
        this.id = idx.getAndIncrement() % workerCount;
        System.out.println("worker id : " + id + "启动");
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (selector.select(10) > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();  // 从 '多路复用器' 取出 '有效' 的key
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandle(key);
                        } else if (key.isReadable()) {
                            readHandle(key);
                        }
                    }
                }
                if (!boss && !queue[id].isEmpty()){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                    SocketChannel client = queue[id].take();
                    client.register(selector, SelectionKey.OP_READ, byteBuffer);

                    System.out.println("--------------------------------------------------");
                    System.out.println("新客户端: " + client.getRemoteAddress() + "分配给 worker : " + id);
                    System.out.println("--------------------------------------------------");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void acceptHandle(SelectionKey selectionKey){
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);

            int id = idx.getAndIncrement() % workerCount;
            queue[id].add(client);

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
}
