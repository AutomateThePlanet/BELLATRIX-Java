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

package solutions.bellatrix.android.services;

import lombok.SneakyThrows;

import java.io.File;

public class FileSystemService extends MobileService {
    public byte[] PullFile(String pathOnDevice) {
        byte[] result = getWrappedAndroidDriver().pullFile(pathOnDevice);
        return result;
    }

    public byte[] pullFolder(String remotePath) {
        byte[] result = getWrappedAndroidDriver().pullFolder(remotePath);
        return result;
    }

    @SneakyThrows
    public void pushFile(String pathOnDevice, File file) {
        getWrappedAndroidDriver().pushFile(pathOnDevice, file);
    }

    public void pushFile(String pathOnDevice, byte[] base64Data) {
        getWrappedAndroidDriver().pushFile(pathOnDevice, base64Data);
    }
}
