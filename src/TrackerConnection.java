import comms.Request;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by malsokait on 2015-12-02.
 */
public class TrackerConnection extends Thread {
    private int port;
    private InetAddress address;
    private Request request;

    public TrackerConnection(InetAddress address, int port, Request request) {
        this.address = address;
        this.port = port;
        this.request = request;
    }

    public boolean connect() {
        try (Socket socket = new Socket(address, port)) {
            socket.setSoTimeout(5000);
            if (socket.isConnected()) {
                Util.sendRequest(socket, request);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void run() {
        if (connect())
            System.out.println("Sent request: " + request.toString() + " to address " + address.getHostAddress());
        else
            System.out.println("Failed to send request: " + request.toString() + " to address " + address.getHostAddress());

        this.interrupt();
    }


}
