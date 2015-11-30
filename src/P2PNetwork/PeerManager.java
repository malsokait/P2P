package P2PNetwork;

import P2PNetwork.comms.*;
import P2PNetwork.events.PeerConnectEvent;
import com.google.common.base.Splitter;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by malsokait on 2015-11-30.
 */
public class PeerManager {
    private Peer peer;
    private static EventBus peerEventBus;
    private PeerServer peerServer;

    public PeerManager(){
        this.peer = new Peer();
        peerEventBus = new EventBus("PeerManager EventBus");
        peerEventBus.register(this);
        this.peerServer = new PeerServer(peer.getPort());
        peerServer.startPeerServer();
    }

    @Subscribe
    public void listen(PeerConnectEvent connectEvent){
        processRequest(connectEvent.getRequest());
    }

    public void processRequest(Request request){
        if(request instanceof FileReply){
            processFileReply((FileReply) request);
        } else if (request instanceof DownloadFileRequest) {
            processDownloadFileRequest((DownloadFileRequest) request);
        }
    }

    public boolean joinNetwork(){
        try (Socket socket = new Socket(Tracker.HOST, Tracker.PORT)){
            Request request = new JoinRequest(peer);
            if(Util.sendRequest(socket, request))
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerFile(String f){
        List list = Splitter.on('.').splitToList(f);
        if(list.size() == 2) {
            String fileName = (String) list.get(0);
            String fileExtension = (String) list.get(1);
            File file = new File(f);
            if(file.isFile()){
                try(Socket socket = new Socket(Tracker.HOST, Tracker.PORT)){
                    Request request = new RegisterFileRequest(peer, f);
                    if(Util.sendRequest(socket, request))
                        return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File doesn't exist");
            }
        } else {
            System.out.println("Incorrect file name");
        }
        return false;
    }

    public void processFileReply(FileReply reply){
        System.out.println("Received download file reply for file " + reply.getFileName());
        System.out.println("*************************************** PEERS:");
        for(Peer p : reply.getPeers()){
            System.out.println(p.toString());
        }
        downloadFile(reply.getPeers().iterator().next(), reply.getFileName());
    }

    public void requestFile(String fileName){
        try (Socket socket = new Socket(Tracker.HOST, Tracker.PORT)){
            Request request = new FileRequest(peer, fileName);
            if(Util.sendRequest(socket, request))
                System.out.println("Requested file from tracker: " + fileName);
        } catch (IOException e) {
            System.out.println("Couldn't connect to tracker.");
        }
    }

    public void downloadFile(Peer seeder, String fileName){
        int listenPort = Util.getFreePort();
        File file = new File(peer.getId() + "_" + fileName);
        try (Socket socket = new Socket(seeder.getAddress(), seeder.getPort())){
            if (file.createNewFile()){
                PeerDownload peerDownload = new PeerDownload(seeder, file, listenPort);
                peerDownload.startDownload();
                Request request = new DownloadFileRequest(peer, fileName, listenPort);
                if (Util.sendRequest(socket, request))
                    System.out.println("Downloading file: " + fileName + " from " + seeder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processDownloadFileRequest(DownloadFileRequest request){
        PeerUpload peerUpload = new PeerUpload(request.getPeer(), request.getListenPort(), request.getFileName());
        peerUpload.startUpload();
        System.out.println("Uploading file: " + request.getFileName() + " to: " + request.getPeer().toString());
    }


    public static EventBus getPeerEventBus() {
        return peerEventBus;
    }
}
