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

package solutions.bellatrix.android.infrastructure;

import plugins.video.VideoPlugin;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.UserHomePathNormalizer;
import solutions.bellatrix.android.configuration.AndroidSettings;

import java.io.File;
import java.util.UUID;

public class MobileVideoPlugin extends VideoPlugin {
    public MobileVideoPlugin(boolean isEnabled) {
        super(isEnabled);
    }

    public static MobileVideoPlugin of() {
        boolean isEnabled = ConfigurationService.get(AndroidSettings.class).getVideosOnFailEnabled();
        return new MobileVideoPlugin(isEnabled);
    }

    @Override
    protected String getOutputFolder() {
        String saveLocation = ConfigurationService.get(AndroidSettings.class).getVideosSaveLocation();
        saveLocation = UserHomePathNormalizer.normalizePath(saveLocation);

        var directory = new File(saveLocation);
        if (! directory.exists()){
            directory.mkdirs();
        }

        return saveLocation;
    }

    @Override
    protected String getUniqueFileName(String testName) {
        return testName.concat(UUID.randomUUID().toString());
    }
}
