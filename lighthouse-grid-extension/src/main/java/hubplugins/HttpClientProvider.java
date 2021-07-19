package hubplugins;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientProvider {

    public CloseableHttpClient provide() {
        return HttpClients.createDefault();
    }
}
