package P2PNetwork;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by malsokait on 2015-11-29.
 */
public class PeerUpload extends Thread {
    private Peer leecher;
    private int listenPort;
    private File file;

    public PeerUpload(Peer leecher, int listenPort, String fileName){
        this.leecher = leecher;
        this.listenPort = listenPort;
        this.file = new File(fileName);
    }

    public void startUpload(){
        try(Socket socket = new Socket(leecher.getAddress(), listenPort)) {
            if(file.isFile()){
                System.out.println("Processing file upload " + file.getName() + " to " + leecher.toString());
                ByteSource byteSource = Files.asByteSource(file);
                OutputStream fileOutputStream = socket.getOutputStream();
                fileOutputStream.write(byteSource.read());
                finishUpload();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finishUpload(){
        this.interrupt();
    }

    @Override
    public void run() {
        startUpload();
    }
}
