import comms.*;
import events.PeerConnectEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by malsokait on 2015-11-30.
 */
public class PeerManager {
    private static EventBus peerEventBus;
    private Peer peer;
    private PeerServer peerServer;

    public PeerManager() {
        this.peer = new Peer();
        peerEventBus = new EventBus("PeerManager EventBus");
        peerEventBus.register(this);
        this.peerServer = new PeerServer(peer.getPort());
        peerServer.startPeerServer();
    }

    public static EventBus getPeerEventBus() {
        return peerEventBus;
    }

    @Subscribe
    public void listen(PeerConnectEvent connectEvent) {
        processRequest(connectEvent.getRequest());
    }

    public void processRequest(Request request) {
        if (request instanceof FileReply) {
            processFileReply((FileReply) request);
        } else if (request instanceof DownloadFileRequest) {
            processDownloadFileRequest((DownloadFileRequest) request);
        }
    }

    public boolean joinNetwork() {
        try (Socket socket = new Socket(Tracker.HOST, Tracker.PORT)) {
            Request request = new JoinRequest(peer);
            if (Util.sendRequest(socket, request)) {
                System.out.println("Connected to tracker.");
                return true;
            } else {
                System.out.println("Failed to connect to the tracker.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerFile(String f) {
        File file = new File(f);
        if (file.isFile()) {
            try (Socket socket = new Socket(Tracker.HOST, Tracker.PORT)) {
                Request request = new RegisterFileRequest(peer, f);
                if (Util.sendRequest(socket, request)) {
                    System.out.println("Registered file: " + f);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File doesn't exist");
        }
        return false;
    }

    public void processFileReply(FileReply reply) {
        boolean downloadConnection = false;
        int counter = 1;
        System.out.println("Received download file reply for file " + reply.getFileName());
        System.out.println("Number of seeders: " + reply.getPeers().size());
        System.out.println("------------------------------------------------------------");
        for (Peer p : reply.getPeers()) {
            System.out.println("Peer: " + p.getId().toString());
            System.out.println("Address: " + p.getAddress().toString());
            System.out.println("------------------------------------------------------------");
        }
        while (!downloadConnection && counter <= reply.getPeers().size()) {
            for (Peer p : reply.getPeers()) {
                System.out.println("Connecting to " + p.getId().toString());
                if (!downloadFile(p, reply.getFileName())) {
                    System.out.println("    Peer offline. ");
                    counter++;
                } else {
                    downloadConnection = true;
                }

            }
        }
        if (!downloadConnection)
            System.out.println("All seeders were offline.");
    }

    public void requestFile(String fileName) {
        try (Socket socket = new Socket(Tracker.HOST, Tracker.PORT)) {
            Request request = new FileRequest(peer, fileName);
            if (Util.sendRequest(socket, request))
                System.out.println("Requested file from tracker: " + fileName);
        } catch (IOException e) {
            System.out.println("Couldn't connect to tracker.");
        }
    }

    public boolean downloadFile(Peer seeder, String fileName) {
        int listenPort = Util.getFreePort();
        File file = new File(peer.getId() + "_" + fileName);
        int renameCounter = 1;
        while (file.isFile()) {
            file = new File("(" + renameCounter + ")" + file.getName());
            renameCounter++;
        }
        try (Socket socket = new Socket(seeder.getAddress(), seeder.getPort())) {
            if (file.createNewFile()) {
                PeerDownload peerDownload = new PeerDownload(seeder, file, listenPort);
                Thread t = new Thread(peerDownload);
                t.start();
                Request request = new DownloadFileRequest(peer, fileName, listenPort);
                if (Util.sendRequest(socket, request)) {
                    System.out.println("Downloading file: " + fileName + " from " + seeder.getId().toString());
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;

    }

    public void processDownloadFileRequest(DownloadFileRequest request) {
        PeerUpload peerUpload = new PeerUpload(request.getPeer(), request.getListenPort(), request.getFileName());
        Thread t = new Thread(peerUpload);
        t.start();
        System.out.println("Uploading file: " + request.getFileName() + " to: " + request.getPeer().getAddress().getHostAddress() + ":" + request.getListenPort());
    }

    public Peer getPeer() {
        return peer;
    }
}
