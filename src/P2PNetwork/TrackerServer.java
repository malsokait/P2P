package P2PNetwork;

import P2PNetwork.events.TrackerConnectEvent;
import P2PNetwork.comms.*;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by malsokait on 2015-11-29.
 */
public class TrackerServer extends Thread {
    private int port;
    private ServerSocket serverSocket;
    private boolean run = false;
    private EventBus eventBus;


    public TrackerServer(int port){
        this.port = port;
        eventBus = Tracker.getEventBus();
        eventBus.register(this);
    }

    public void startServerSocket(){
        try{
            serverSocket = new ServerSocket(port);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServerSocket(){
        run = false;
        this.interrupt();
    }

    @Override
    public void run(){
        run = true;
        while (run){
            try {
                System.out.println("Listening on: " + serverSocket.getInetAddress().toString());
                Socket socket = serverSocket.accept();
                Request request = (Request) new ObjectInputStream(socket.getInputStream()).readObject();
                eventBus.post(new TrackerConnectEvent(request));

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
