package io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO
 */
public class SocketIO {

    public static void main(String[] args) throws Exception {

        /*
          socket() = 6fd ;
          bind(6fd, 8090) ;
          listen(6fd)
         */
        ServerSocket server = new ServerSocket(8090);

        System.out.println("step 1: new server_socket : 8090");

        /*
         accept(6fd); ==> (阻塞)
                      ==> 7fd
         */
        Socket client = server.accept();

        System.out.println("step 2 : accept client, client port : " + client.getPort());

        /*
         read(7fd)
         阻塞
         */
        InputStream inputStream = client.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        System.out.println("accept request : " + bufferedReader.readLine());

        while (true){

        }
    }
}
