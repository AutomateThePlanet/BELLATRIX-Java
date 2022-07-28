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

package solutions.bellatrix.ios.infrastructure;

import plugins.video.VideoPlugin;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.PathNormalizer;
import solutions.bellatrix.ios.configuration.IOSSettings;

import java.io.File;
import java.util.UUID;

public class MobileVideoPlugin extends VideoPlugin {
    public MobileVideoPlugin() {
        super(ConfigurationService.get(IOSSettings.class).getVideosOnFailEnabled());
    }

    @Override
    protected String getOutputFolder() {
        String saveLocation = ConfigurationService.get(IOSSettings.class).getVideosSaveLocation();
        saveLocation = PathNormalizer.normalizePath(saveLocation);

        var directory = new File(saveLocation);

        if (directory.mkdirs()) {
            return saveLocation;
        }

        return saveLocation;
    }

    @Override
    protected String getUniqueFileName(String testName) {
        return testName.concat(UUID.randomUUID().toString());
    }
}
