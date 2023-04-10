package io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 总结NIO 的好处
 * 连接, 读, 写 都是异步的
 * JDK的selector在 linux上通过 epoll 实现, 没有连接句柄数的限制
 * (只受限于操作系统的最大句柄数或者对单个进程的句柄数限制)
 */
public class NioTimerServer {
    public static void main(String[] args) {
        new Thread(new MultiplexerTimeServer(8080),"LYK_MultiplexerTimeServer-001").start();
    }
}
class MultiplexerTimeServer implements Runnable{
    private Selector selector;

    private volatile boolean stop;
    
    /**
     * 初始化多路复用器, 绑定监听端口
     * @param port 监听端口
     */

    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Nio time server start, port : " + port + ".");
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop){
            try {
                // 休眠时间 1s
                selector.select(1000);
                // 返回 '就绪状态' 的 Channel的 SelectionKey集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    }catch (IOException e){
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
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

    /**
     * 根据SelectionKey的操作位判断网络事件的类型
     */
    private void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            // 处理新接入的连接
            if(key.isAcceptable()){
                try {
                    ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                    // TCP三次握手, TCP物理链路正式建立
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    /*
                     * 这里还可以对其TCP参数进行设置; 例如: TCP 接受和发送缓冲区的大小等
                     */
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 处理请求数据
            if(key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                // 由于sc.configureBlocking(false); 所以这里'读'是不阻塞的
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0){
                    // 将缓冲区当前的limit => position, 将position => 0
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String requestBody = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("Nio Timer Server receive request : " + requestBody);
                    String currentTimeStr = requestBody.equalsIgnoreCase("query time") ?
                            new Date(System.currentTimeMillis()).toString() : "bad order";
                    doWrite(sc, currentTimeStr);
                }else if(readBytes < 0){
                    // 对端链路关闭; -1 表示链路已经关闭了
                    key.cancel();
                    sc.close();
                }else {
                    ; // 读到0字节, 忽略 (属于正常场景)
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        if(response == null || response.trim().length() <= 0){
            return;
        }
        byte[] bytes = response.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        // 由于SocketChannel 是异步非阻塞的, 并不能保证一次就把字节数组发送完, 此时会出现"写半包"的问题
        // 需要注册 '写操作', 不断轮询selector 将没有发送完的 ByteBuffer发送完
        // 可以通过 ByteBuffer.hasRemain() 判断消息是否发送完成
        channel.write(writeBuffer);
    }
}
