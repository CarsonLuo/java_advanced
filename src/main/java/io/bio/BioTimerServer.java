package io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
