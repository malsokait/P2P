package comms;

import Peer;
import com.google.common.collect.ImmutableSet;

/**
 * Created by malsokait on 2015-11-29.
 */
public class FileReply extends Request {
    private ImmutableSet<Peer> peers;
    private String fileName;

    public FileReply(Peer peer, String fileName, ImmutableSet<Peer> peers) {
        super(peer);
        this.fileName = fileName;
        this.peers = peers;
    }

    public ImmutableSet<Peer> getPeers() {
        return peers;
    }

    public String getFileName() {
        return fileName;
    }


}
