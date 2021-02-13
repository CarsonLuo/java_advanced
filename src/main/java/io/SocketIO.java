package io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/*
   BIO
 */
public class SocketIO {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8090);

        System.out.println("step 1: new server_socket : 8090");

        Socket client = server.accept();

        System.out.println("step 2 : accept client, client port : " + client.getPort());

        InputStream inputStream = client.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        System.out.println("accept request : " + bufferedReader.readLine());

        while (true){

        }
    }
}
