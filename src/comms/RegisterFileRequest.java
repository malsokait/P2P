package comms;

import Peer;

/**
 * Created by malsokait on 2015-11-29.
 */
public class RegisterFileRequest extends Request {
    private String fileName;

    public RegisterFileRequest(Peer peer, String fileName) {
        super(peer);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
