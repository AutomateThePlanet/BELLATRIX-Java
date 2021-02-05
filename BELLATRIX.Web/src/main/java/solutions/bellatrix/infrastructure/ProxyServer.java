/*
 * Copyright 2021 Automate The Planet Ltd.
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

package solutions.bellatrix.infrastructure;

import lombok.SneakyThrows;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.testng.Assert;

import java.net.ServerSocket;

public class ProxyServer {
    private static ThreadLocal<BrowserMobProxyServer> proxyServer = new ThreadLocal<>();

    @SneakyThrows
    public static int init() {
        var s = new ServerSocket(0);
        int port = s.getLocalPort();
        proxyServer.set(new BrowserMobProxyServer());
        proxyServer.get().start(port);
        return port;
    }

    public static void close() {
        if (proxyServer.get() != null) {
            proxyServer.get().stop();
        }
    }

    public void assertNoErrorCodes() {
        var harEntries = proxyServer.get().getHar().getLog().getEntries();
        var areThereErrorCodes = harEntries.stream().anyMatch(r
                -> r.getResponse().getStatus() > 400
                && r.getResponse().getStatus() < 599);

        Assert.assertFalse(areThereErrorCodes);
    }

    public void assertRequestMade(String url) {
        var harEntries = proxyServer.get().getHar().getLog().getEntries();
        var areRequestsMade = harEntries.stream().anyMatch(r -> r.getRequest().getUrl().contains(url));

        Assert.assertTrue(areRequestsMade);
    }

    public void assertNoLargeImagesRequested() {
        var harEntries = proxyServer.get().getHar().getLog().getEntries();
        var areThereLargeImages = harEntries.stream().anyMatch(r
                -> r.getResponse().getContent().getMimeType().startsWith("image")
                && r.getResponse().getContent().getSize() > 40000);

        Assert.assertFalse(areThereLargeImages);
    }
}
