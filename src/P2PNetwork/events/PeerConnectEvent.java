package P2PNetwork.events;

import P2PNetwork.comms.Request;

/**
 * Created by malsokait on 2015-11-29.
 */
public class PeerConnectEvent {
    private Request request;

    public PeerConnectEvent(Request request){
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
