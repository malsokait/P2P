package P2PNetwork;

import P2PNetwork.requests.JoinRequest;
import P2PNetwork.requests.Request;
import com.google.common.base.MoreObjects;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;


/**
 * Created by malsokait on 2015-11-29.
 */
public class Peer implements Serializable{
    private UUID id;
    private InetAddress address;
    private int port;

    public Peer(){
        this.id = UUID.randomUUID();
        this.address = Util.getHost();
        this.port = Util.getFreePort();
    }

    public boolean joinNetwork(){
        try (Socket socket = new Socket(Tracker.HOST, Tracker.PORT)){
            Request request = new JoinRequest(this);
            if(Util.sendRequest(socket, request))
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("address", address)
                .add("port", port)
                .toString();
    }
}
