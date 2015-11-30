package P2PNetwork;

import P2PNetwork.comms.Request;
import P2PNetwork.events.PeerConnectEvent;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by malsokait on 2015-11-29.
 */
public class PeerServer extends Thread {
    private int port;
    private ServerSocket serverSocket;
    private boolean run;
    private EventBus peerEventBus;

    public PeerServer(int port){
        this.port = port;
        peerEventBus = PeerManager.getPeerEventBus();
        peerEventBus.register(this);
    }

    public void startPeerServer(){
        try {
            serverSocket = new ServerSocket(port);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPeerServer(){
        run = false;
        this.interrupt();
    }

    @Override
    public void run(){
        run = true;
        while (run){
            try {
                Socket socket = serverSocket.accept();
                Request request = (Request) new ObjectInputStream(socket.getInputStream()).readObject();
                peerEventBus.post(new PeerConnectEvent(request));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
