package core;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;


/**
 * Created by malsokait on 2015-11-29.
 */
public class Peer implements Serializable {
    private UUID id;
    private InetAddress address;
    private int port;


    public Peer() {
        this.id = UUID.randomUUID();
        this.address = Util.getLocalhost();
        this.port = Util.getFreePort();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("address", address)
                .add("port", port)
                .toString();
    }

    public UUID getId() {
        return id;
    }


    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }


}
