package hubplugins;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class HubRequestsProxyingServlet extends RegistryBasedServlet {

    private static final Logger LOGGER = Logger.getLogger(HubRequestsProxyingServlet.class.getName());

    @SuppressWarnings("unused")
    public HubRequestsProxyingServlet() {
        this(null);
    }

    public HubRequestsProxyingServlet(GridRegistry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String responseToClient= retrieveRemoteHost(req.getPathInfo());

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(responseToClient);
        resp.getWriter().flush();
    }

    private String retrieveRemoteHost(String path) {
        LOGGER.info("Forwarding request with path: " + path);
        String sessionId = SeleniumSessions.getSessionIdFromPath(path);
        LOGGER.info("Retrieving remote host for session: " + sessionId);

        SeleniumSessions sessions = new SeleniumSessions(getRegistry());
        sessions.refreshTimeout(sessionId);

        URL remoteHost = sessions.getRemoteHostForSession(sessionId);
        String host = remoteHost.getHost();
        int port = remoteHost.getPort();
        LOGGER.info("Remote host retrieved: " + host + ":" + port);

        return host + ":" + port;
    }
}
