package hubplugins;

public class RequestForwardingClientProvider {
    public hubplugins.RequestForwardingClient provide(String host, int port) {
        return new hubplugins.RequestForwardingClient(host, port);
    }
}
