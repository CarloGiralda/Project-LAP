package LAP.Blockchain.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

// only for receiving messages
public class SocketServer {
    private int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            System.out.println("Server is listening on port " + port);

            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            try {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                while (true) {
                    // TODO this response must be passed to the blockchain service
                    String response = reader.readLine();
                }
            } catch (IOException ex) {
                System.out.println("Server thread exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
