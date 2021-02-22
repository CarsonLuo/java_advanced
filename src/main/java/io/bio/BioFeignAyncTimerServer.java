package io.bio;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * BIO, 伪异步
 */
public class BioFeignAyncTimerServer {
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
