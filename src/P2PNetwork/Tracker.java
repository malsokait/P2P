package P2PNetwork;

import P2PNetwork.comms.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import P2PNetwork.events.TrackerConnectEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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
    private Multimap<String, Peer> filesMap;
    private TrackerServer trackerServer;
    private static EventBus eventBus = new EventBus("Tracker EventBus");

    public Tracker(){
        this.port = PORT;
        this.host = HOST;
        this.peers = new ArrayList<>();
        this.trackerServer = new TrackerServer(port);
        trackerServer.startServerSocket();
        eventBus.register(this);
        filesMap = LinkedListMultimap.create();
    }

    @Subscribe
    public void listen(TrackerConnectEvent connectEvent){
        processRequest(connectEvent.getRequest());
    }

    public void processRequest(Request request){
        if(request instanceof JoinRequest){
            peers.add(request.getPeer());
            System.out.println("Added peer: " + request.getPeer().toString());
        } else if (request instanceof RegisterFileRequest){
            processRegisterFileRequest((RegisterFileRequest) request);
        } else if (request instanceof FileRequest){
            processFileRequest((FileRequest) request);
        }
    }

    public void processRegisterFileRequest(RegisterFileRequest request){
        filesMap.put(request.getFileName(), request.getPeer());
        System.out.println("Added file: " + request.getFileName() + " from peer: " + request.getPeer().toString() );
    }

    public void processFileRequest(FileRequest request){
        Peer peer = request.getPeer();
        if(filesMap.containsKey(request.getFileName())){
            ImmutableSet<Peer> peers = ImmutableSet.copyOf(filesMap.get(request.getFileName()));
            try(Socket socket = new Socket(peer.getAddress(), peer.getPort())){
                Request reply = new FileReply(peer, request.getFileName(), peers);
                if(Util.sendRequest(socket, reply)){
                    StringBuilder msg = new StringBuilder();
                    msg.append("Sent download file reply to peer: ");
                    msg.append(peer.toString());
                    msg.append(", requested file: ");
                    msg.append(request.getFileName());
                    System.out.println(msg.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Couldn't connect to peer: " + peer.toString());
            }
        }
    }

    public static EventBus getEventBus(){
        return eventBus;
    }

    public ArrayList<Peer> getPeers() {
        return peers;
    }
}
