package io.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;

/**
 * BIO : 时间服务
 */
public class BioTimerServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(args != null && args.length > 1){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("Time Server start, port : " + port + ".");
            while (true){
                Socket socket = server.accept();
                new Thread(new Handler(socket)).start();
            }
        }finally {
            if(server != null){
                System.out.println("Time Server end.");
                server.close();
            }
        }
    }
}

class Handler implements Runnable{
    private final Socket socket;

    public Handler(Socket socket){
        this.socket = socket;
    }

    /**
     * InputStream.read() 阻塞
     * This method blocks until input data is
     * available, end of file is detected, or an exception is thrown.
     * 一直阻塞, 直到 (1) 有数据可读 (2) 可用数据读取完毕 (3) 抛出异常
     * 如果"请求方" 回复很慢 或者 网络延时, 那就会一直阻塞住
     *
     * OutputStream.write() 阻塞
     * 直到 (1) 所有数据发送完毕 (2) 抛出异常
     *
     * 根据TCP的特性
     * 如果消息接收方不能快速处理消息,不能及时从TCP缓冲区读取数据
     * 会导致消息发送方的TCP window size 不断减小, 直至为0, 双方处于keep-alive状态,
     * 消息发送方将不能向TCP缓冲区写数据了, 如果使用阻塞I/O, write会无限阻塞, 直到TCP window size 大于0 或者发生I/O异常
     */
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {

            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            while (true){
                String requestBody = in.readLine();
                if(requestBody == null){
                    break;
                }
                System.out.println("time server accept request order : " + requestBody);
                String currentTimeStr = requestBody.equalsIgnoreCase("query time") ?
                        new Date(System.currentTimeMillis()).toString() : "bad order";
                out.println(currentTimeStr);
            }
        } catch (IOException e) {
            if(in != null){
                try {
                    in.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if(out != null){
                out.close();
            }
            // socket close
            try {
                this.socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
