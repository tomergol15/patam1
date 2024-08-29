package test;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
private final int port;
private final ClientHandler ch;
private volatile boolean running;


public MyServer(int port1, ClientHandler ch1){
    this.port = port1;
    this.ch = ch1;
}

public void start(){
    running = true;
   new Thread(() -> {
       try {
         ServerSocket serverSocket = new ServerSocket(port);
         serverSocket.setSoTimeout(1000);
           while (running) {
               try {
                   Socket clientSocket = serverSocket.accept();
                   //The server passes the client's Input and Output streams to a ClientHandler instance,
                   // which handles the client's requests and returns responses.
                   ch.handleClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
                   clientSocket.close(); //also close InputStram, OutputStream
               } catch (IOException e) {
                    //System.out.println("Error handling client: " + e.getMessage());
               }
           }
       } catch (IOException e) {
           throw new RuntimeException(e);
       } finally {
           close();
       }
   }).start();
}

public void close(){
    if(running==false){
        return;
    }
    running=false;
}
}
