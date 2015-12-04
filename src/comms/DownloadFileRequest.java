package comms;

import Peer;

/**
 * Created by malsokait on 2015-11-30.
 */
public class DownloadFileRequest extends Request {
    private String fileName;
    private int listenPort;

    public DownloadFileRequest(Peer peer, String fileName, int listenPort) {
        super(peer);
        this.fileName = fileName;
        this.listenPort = listenPort;
    }

    public String getFileName() {
        return fileName;
    }

    public int getListenPort() {
        return listenPort;
    }
}
