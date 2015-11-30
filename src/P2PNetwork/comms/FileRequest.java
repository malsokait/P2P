package P2PNetwork.comms;

import P2PNetwork.Peer;

/**
 * Created by malsokait on 2015-11-29.
 */
public class FileRequest extends Request {
    private String fileName;

    public FileRequest(Peer peer, String fileName){
        super(peer);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

}
