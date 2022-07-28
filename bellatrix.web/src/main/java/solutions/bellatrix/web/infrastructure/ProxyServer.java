/*
 * Copyright 2022 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.infrastructure;

import lombok.SneakyThrows;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.testng.Assert;

import java.io.IOException;
import java.net.ServerSocket;

public class ProxyServer {
    private static final ThreadLocal<BrowserMobProxyServer> PROXY_SERVER = new ThreadLocal<>();

    @SneakyThrows
    public static int init() {
        int port = findFreePort();
        PROXY_SERVER.set(new BrowserMobProxyServer());
        PROXY_SERVER.get().start(port);
        return port;
    }

    public static void close() {
        if (PROXY_SERVER.get() != null) {
            PROXY_SERVER.get().stop();
        }
    }

    public void assertNoErrorCodes() {
        var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
        var areThereErrorCodes = harEntries.stream().anyMatch(r
                -> r.getResponse().getStatus() > 400
                && r.getResponse().getStatus() < 599);

        Assert.assertFalse(areThereErrorCodes);
    }

    public void assertRequestMade(String url) {
        var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
        var areRequestsMade = harEntries.stream().anyMatch(r -> r.getRequest().getUrl().contains(url));

        Assert.assertTrue(areRequestsMade);
    }

    public void assertNoLargeImagesRequested() {
        var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
        var areThereLargeImages = harEntries.stream().anyMatch(r
                -> r.getResponse().getContent().getMimeType().startsWith("image")
                && r.getResponse().getContent().getSize() > 40000);

        Assert.assertFalse(areThereLargeImages);
    }

    private static int findFreePort() {
        int port = 0;
        // For ServerSocket port number 0 means that the port number is automatically allocated.
        try (ServerSocket socket = new ServerSocket(0)) {
            // Disable timeout and reuse address after closing the socket.
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
        } catch (IOException ignored) {}
        if (port > 0) {
            return port;
        }

        throw new RuntimeException("Could not find a free port");
    }
}
