package LAP.Blockchain.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// only for sending messages
public class SocketClient {
    // initialize socket and input output streams
    private Socket socket;
    private PrintWriter writer;

    // constructor to put ip address and port
    public SocketClient(String address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException i) {
            throw new RuntimeException(i);
        }
    }

    public void send(String message) {
        writer.println(message);
    }

    public void close() {
        // close the connection
        try {
            writer.close();
            socket.close();
        } catch (IOException i) {
            throw new RuntimeException(i);
        }
    }
}
