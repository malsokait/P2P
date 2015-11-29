package P2PNetwork;

import P2PNetwork.requests.JoinRequest;
import P2PNetwork.requests.Request;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import P2PNetwork.events.TrackerConnectEvent;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by malsokait on 2015-11-29.
 */
public class Tracker {
    public static final int PORT = 11000;
    public static final InetAddress HOST = Util.getHost();

    private InetAddress host;
    private int port;
    private ArrayList<Peer> peers;
    private TrackerServer trackerServer;
    private static EventBus eventBus = new EventBus("Tracker EventBus");

    public Tracker(){
        this.port = PORT;
        this.host = HOST;
        this.peers = new ArrayList<>();
        this.trackerServer = new TrackerServer(port);
        trackerServer.startServerSocket();
        eventBus.register(this);
    }

    @Subscribe
    public void listen(TrackerConnectEvent connectEvent){
        processRequest(connectEvent.getRequest());
    }

    public void processRequest(Request request){
        if(request instanceof JoinRequest){
            peers.add(request.getPeer());
            System.out.println("Added peer: " + request.getPeer().toString());
        }
    }

    public static EventBus getEventBus(){
        return eventBus;
    }

    public ArrayList<Peer> getPeers() {
        return peers;
    }
}
