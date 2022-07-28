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

package solutions.bellatrix.ios.services;

import lombok.SneakyThrows;

import java.io.File;

public class FileSystemService extends MobileService {
    public byte[] pullFile(String pathOnDevice) {
        byte[] result = getWrappedIOSDriver().pullFile(pathOnDevice);
        return result;
    }

    public byte[] pullFolder(String remotePath) {
        byte[] result = getWrappedIOSDriver().pullFolder(remotePath);
        return result;
    }

    @SneakyThrows
    public void pushFile(String pathOnDevice, File file) {
        getWrappedIOSDriver().pushFile(pathOnDevice, file);
    }

    public void pushFile(String pathOnDevice, byte[] base64Data) {
        getWrappedIOSDriver().pushFile(pathOnDevice, base64Data);
    }
}
