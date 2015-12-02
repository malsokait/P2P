package P2PNetwork;

import P2PNetwork.comms.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.*;

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
            URL url = new URL("http://bot.whatismyipaddress.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipAddress = in.readLine().trim();
            if(ipAddress.length() > 0)
                return InetAddress.getByName(ipAddress);
            else {
                System.out.println("Couldn't find internet IP address, using local address instead.");
                return InetAddress.getLocalHost();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Returning null inetaddress.");
        return null;
    }

    public static InetAddress getTrackerAddress(){
        try {
            return InetAddress.getByName("159.203.1.117");
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

}
