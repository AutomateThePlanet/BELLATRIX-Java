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

package solutions.bellatrix.web.core.utilities;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.Paths;
import java.util.Arrays;

@UtilityClass
public class FileDownloader {
    @SneakyThrows
    public static void download(String url, String fullFilePath) {
        var file = Paths.get(fullFilePath).toFile();
        if (file.exists())
            return;

        var urlStream = new URL(url).openStream();
        var readableByteChannel = Channels.newChannel(urlStream);
        var fileOutputStream = new FileOutputStream(file);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        urlStream.close();
        fileOutputStream.close();
    }

    @SneakyThrows
    public static String downloadToUsersFolder(String url) {
        var fileName = Arrays.stream(url.split("/")).reduce((first, second) -> second).orElse(null);
        var userHomeDir = System.getProperty("user.home");
        var file = Paths.get(userHomeDir, fileName).toFile();
        if (file.exists())
            return file.getPath();

        var urlStream = new URL(url).openStream();
        var readableByteChannel = Channels.newChannel(urlStream);
        var fileOutputStream = new FileOutputStream(file);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        urlStream.close();
        fileOutputStream.close();

        return file.getPath();
    }
}
