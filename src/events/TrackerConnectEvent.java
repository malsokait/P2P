package events;

import comms.Request;

/**
 * Created by malsokait on 2015-11-29.
 */
public class TrackerConnectEvent {
    private Request request;

    public TrackerConnectEvent(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
