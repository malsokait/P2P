package P2PNetwork;

import P2PNetwork.comms.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by malsokait on 2015-11-29.
 */
public class Util {
    public static Integer getFreePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)){
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InetAddress getHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sendRequest(Socket socket, Request request){
        try (ObjectOutputStream send = new ObjectOutputStream(socket.getOutputStream())) {
            send.writeObject(request);
            return true;
        } catch (IOException e) {
                e.printStackTrace();
        }
        return false;
    }

    public static InetAddress getRemote(){
        try {
            return InetAddress.getByName("192.168.0.20");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
