import comms.*;
import events.TrackerConnectEvent;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by malsokait on 2015-11-29.
 */
public class Tracker {
    public static final int PORT = 11000;
    public static final InetAddress HOST = Util.getLocalhost();
    private static EventBus eventBus = new EventBus("Tracker EventBus");
    private int port;
    private ArrayList<Peer> peers;
    private Multimap<String, Peer> filesMap;
    private TrackerServer trackerServer;

    public Tracker() {
        this.port = PORT;
        this.peers = new ArrayList<>();
        this.trackerServer = new TrackerServer(port);
        trackerServer.startServerSocket();
        eventBus.register(this);
        filesMap = LinkedListMultimap.create();
        System.out.println("Started Tracker server on " + HOST.getHostAddress());
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    @Subscribe
    public void listen(TrackerConnectEvent connectEvent) {
        processRequest(connectEvent.getRequest());
    }

    public void processRequest(Request request) {
        if (request instanceof JoinRequest) {
            peers.add(request.getPeer());
            System.out.println("Registered peer: " + request.getPeer().getId().toString() + ", " + request.getPeer().getAddress().getCanonicalHostName());
        } else if (request instanceof RegisterFileRequest) {
            processRegisterFileRequest((RegisterFileRequest) request);
        } else if (request instanceof FileRequest) {
            processFileRequest((FileRequest) request);
        }
    }

    public void processRegisterFileRequest(RegisterFileRequest request) {
        filesMap.put(request.getFileName(), request.getPeer());
        System.out.println("Added file: " + request.getFileName() + " from peer: " + request.getPeer().getId().toString());
    }

    public void processFileRequest(FileRequest request) {
        Peer peer = request.getPeer();
        if (filesMap.containsKey(request.getFileName())) {
            ImmutableSet<Peer> peers = ImmutableSet.copyOf(filesMap.get(request.getFileName()));
            Request reply = new FileReply(peer, request.getFileName(), peers);
            TrackerConnection trackerConnection = new TrackerConnection(peer.getAddress(), peer.getPort(), reply);
            Thread t = new Thread(trackerConnection);
            t.start();
        }
    }

}
