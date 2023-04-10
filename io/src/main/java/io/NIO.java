package io;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class NIO {

    public static void main(String[] args) throws Exception{
        LinkedList<SocketChannel> clientList = new LinkedList<>();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8080));
        serverChannel.configureBlocking(false);

        System.out.println("step 1 : new server socket channel, port : 8080");

        while (true){
            Thread.sleep(500);
            SocketChannel client = serverChannel.accept();
            if(client == null){
                System.out.println("client is null");
            }else {
                client.configureBlocking(false);
                clientList.add(client);
                System.out.println("accept client, client port : " + client.socket().getPort()
                        + ", add client list, list size : " + clientList.size());
            }
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096); // ByteBuffer.allocate() 分配到堆里, allocateDirect() 分配到对外
            for (SocketChannel c : clientList){
                int num = c.read(byteBuffer);
                if(num > 0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.limit()];
                    byteBuffer.get(bytes);

                    String requestStr = new String(bytes);
                    System.out.println("client port : " + c.socket().getPort() + ", : " + requestStr);
                    byteBuffer.clear();
                }
            }
        }
    }
}