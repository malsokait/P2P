package P2PNetwork;

import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by malsokait on 2015-11-29.
 */
public class PeerDownload extends Thread{
    private File file;
    private int listenPort;
    private ServerSocket serverSocket;
    private Peer peer;
    private Socket socket;

    public PeerDownload(Peer peer, File file, int listenPort){
        this.peer = peer;
        this.file = file;
        this.listenPort = listenPort;
    }

    public void startDownload(){
        try {
            serverSocket = new ServerSocket(listenPort);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            socket = serverSocket.accept();
            System.out.println("Processing file download " + file.getName() + " from " + peer.toString());
            ByteSink byteSink = Files.asByteSink(file);

            byteSink.writeFrom(socket.getInputStream());

            System.out.println("Downloaded file: " + file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
