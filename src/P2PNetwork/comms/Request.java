package P2PNetwork.comms;

import P2PNetwork.Peer;

import java.io.Serializable;

/**
 * Created by malsokait on 2015-11-29.
 */
public abstract class Request implements Serializable{
    private Peer peer;

    public Request(Peer peer){
        this.peer = peer;
    }

    public Peer getPeer() {
        return peer;
    }
}
