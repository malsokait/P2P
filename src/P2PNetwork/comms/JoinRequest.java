package P2PNetwork.comms;


import P2PNetwork.Peer;

import java.io.Serializable;

/**
 * Created by malsokait on 2015-11-29.
 */
public class JoinRequest extends Request implements Serializable {
    public JoinRequest(Peer peer){
        super(peer);
    }
}
