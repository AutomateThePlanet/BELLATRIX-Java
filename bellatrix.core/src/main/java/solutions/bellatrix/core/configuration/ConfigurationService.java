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

package solutions.bellatrix.core.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import solutions.bellatrix.core.utilities.DebugInformation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

@UtilityClass
public final class ConfigurationService {
    private static String environment;

    public static String getEnvironment() {
        return environment;
    }

    public static <T> T get(Class<T> configSection) {
        T mappedObject = (T)new Object();
        if (environment == null) {
            String environmentOverride = System.getProperty("environment");
            if (environmentOverride == null) {
                InputStream input = ConfigurationService.class.getResourceAsStream("/application.properties");
                var p = new Properties();
                try {
                    p.load(input);
                } catch (IOException e) {
                    return mappedObject;
                }

                environment = p.getProperty("environment");
            } else {
                environment = environmentOverride;
            }
        }

        String fileName = String.format("testFrameworkSettings.%s.json", environment);
        String jsonFileContent = getFileAsString(fileName);
        String sectionName = getSectionName(configSection);

        var jsonObject = JsonParser.parseString(jsonFileContent).getAsJsonObject().get(sectionName).toString();

        var gson = new Gson();

        try {
            mappedObject = gson.fromJson(jsonObject, configSection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mappedObject;
    }

    public static String getSectionName(Class<?> configSection) {
        var sb = new StringBuilder(configSection.getSimpleName());
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

  //  @SneakyThrows
    public static String getFileAsString(String fileName) {
        InputStream input = ConfigurationService.class.getResourceAsStream("/" + fileName);
        try {
            return IOUtils.toString(Objects.requireNonNull(input), StandardCharsets.UTF_8);
        } catch (IOException e) {
            DebugInformation.printStackTrace(new IOException("Couldn't load '" + fileName + "'"));
            return "";
        }
    }
}