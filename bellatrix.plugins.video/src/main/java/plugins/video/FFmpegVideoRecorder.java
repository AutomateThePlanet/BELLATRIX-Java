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

package plugins.video;

import org.apache.commons.io.FilenameUtils;
import solutions.bellatrix.core.utilities.FileDownloader;
import solutions.bellatrix.core.utilities.RuntimeInformation;

import java.nio.file.Paths;

public class FFmpegVideoRecorder implements AutoCloseable {
    @Override
    public void close() {
        try {
            if (RuntimeInformation.IS_WINDOWS) {
                Runtime.getRuntime().exec("taskkill /IM ffmpeg_windows.exe /F");
            } else if (RuntimeInformation.IS_MAC) {
                Runtime.getRuntime().exec("killall ffmpeg_osx");
            } else {
                Runtime.getRuntime().exec("killall ffmpeg_linux");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String startRecording(String videoSaveDir, String videoFileName) {
        var videoFullPath = Paths.get(videoSaveDir, videoFileName).toString();
        try {
            if (RuntimeInformation.IS_WINDOWS) {
                var recorderFile = FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.0/ffmpeg_windows.exe");
                var videoFilePathWithExtension = String.format("%s.mpg", FilenameUtils.removeExtension(videoFullPath));
                Runtime.getRuntime().exec(recorderFile + " -f gdigrab" + " -framerate 30" + String.format(" -i desktop %s", videoFilePathWithExtension));
            } else if (RuntimeInformation.IS_MAC) {
                var recorderFile = FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.0/ffmpeg_osx");
                var videoFilePathWithExtension = String.format("%s.mkv", FilenameUtils.removeExtension(videoFullPath));
                Runtime.getRuntime().exec(recorderFile + " -f avfoundation" + " -framerate 10" + String.format(" -i \"0:0\" %s", videoFilePathWithExtension));
            } else {
                var recorderFile = FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.0/ffmpeg_linux");
                var videoFilePathWithExtension = String.format("%s.mp4", FilenameUtils.removeExtension(videoFullPath));
                Runtime.getRuntime().exec(recorderFile + " -f x11grab" + " -framerate 30" + String.format(" -i :0.0+100,200 %s", videoFilePathWithExtension));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videoFullPath;
    }
}
