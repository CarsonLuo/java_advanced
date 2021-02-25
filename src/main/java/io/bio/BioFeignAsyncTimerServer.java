package io.bio;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * BIO, 伪异步
 * 由于read/write都是阻塞的, 阻塞时间取决于对方I/O处理速度,网络I/O的处理速度
 *
 * 假如通信对方应答时间很长, 线程池中的所有线程都会被阻塞, 队列中的消息也就没法消化了
 */
public class BioFeignAsyncTimerServer {
    private final static ThreadPoolExecutor POOL_EXECUTOR =
            new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws Exception{
        int port = 8080;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("Time Server start, port : " + port + ".");
            while (true){
                Socket socket = server.accept();
                POOL_EXECUTOR.execute(new Handler(socket));
            }
        }finally {
            if(server != null){
                System.out.println("Time Server end.");
                server.close();
            }
        }
    }
}
