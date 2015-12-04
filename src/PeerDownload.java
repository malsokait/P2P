import com.google.common.io.ByteSink;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by malsokait on 2015-11-29.
 */
public class PeerDownload extends Thread {
    private File file;
    private int listenPort;
    private Peer peer;
    private Socket socket;

    public PeerDownload(Peer peer, File file, int listenPort) {
        this.peer = peer;
        this.file = file;
        this.listenPort = listenPort;
    }

    public void startDownload() {
        try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
            socket = serverSocket.accept();

            ByteSink byteSink = Files.asByteSink(file);

            byteSink.writeFrom(socket.getInputStream());

            System.out.println("Downloaded file: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            finishDownload();
        }
    }

    public void finishDownload() {
        this.interrupt();
    }

    @Override
    public void run() {
        startDownload();
    }
}
