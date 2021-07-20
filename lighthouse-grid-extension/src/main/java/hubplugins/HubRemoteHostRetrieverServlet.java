package hubplugins;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

// java -Dwebdriver.chrome.driver="C:\Users\angel\.nuget\packages\selenium.webdriver.chromedriver\91.0.4472.10100\driver\win32\chromedriver.exe" -cp "selenium-server-standalone-3.141.59.jar;bellatrix-selenium-grid-extensions.jar" org.openqa.grid.selenium.GridLauncherV3 -role hub -servlets "hubplugins.HubRemoteHostRetrieverServlet"
public class HubRemoteHostRetrieverServlet extends RegistryBasedServlet {

    private static final Logger LOGGER = Logger.getLogger(HubRemoteHostRetrieverServlet.class.getName());

    @SuppressWarnings("unused")
    public HubRemoteHostRetrieverServlet() {
        this(null);
    }

    public HubRemoteHostRetrieverServlet(GridRegistry registry) {
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
