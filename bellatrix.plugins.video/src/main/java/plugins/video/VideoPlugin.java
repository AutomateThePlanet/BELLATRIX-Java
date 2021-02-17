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

package plugins.video;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import solutions.bellatrix.plugins.EventListener;
import solutions.bellatrix.plugins.Plugin;
import solutions.bellatrix.plugins.TestResult;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;

public abstract class VideoPlugin extends Plugin {
    public static final EventListener<VideoPluginEventArgs> VIDEO_GENERATED = new EventListener<>();
    private static final FFmpegVideoRecorder FMPEG_VIDEO_RECORDER = new FFmpegVideoRecorder();
    private static final ThreadLocal<String> VIDEO_FULL_PATH = new ThreadLocal<>();
    private Boolean isEnabled;

    public VideoPlugin(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    protected abstract String getOutputFolder();
    protected abstract String getUniqueFileName(String testName);

    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        if (isEnabled) {
            try {
                var videoSaveDir = getOutputFolder();
                var videoFileName = getUniqueFileName(memberInfo.getName());
                takeVideo(videoSaveDir, videoFileName);

            } catch (Exception e) {
                // ignore since it is failing often because of bugs in Remote driver for Chrome
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public void preAfterTest(TestResult testResult, Method memberInfo) {
        if (isEnabled) {
            var videoSaveDir = getOutputFolder();
            var videoFileName = getUniqueFileName(memberInfo.getName());
            var videoFullPath = Paths.get(videoSaveDir, videoFileName).toString();
            FMPEG_VIDEO_RECORDER.close();
            if (testResult == TestResult.FAILURE) {
                VIDEO_GENERATED.broadcast(new VideoPluginEventArgs(VIDEO_FULL_PATH.get()));
            } else {
                FileUtils.forceDeleteOnExit(new File(videoFullPath));
            }
        }
    }

    protected void takeVideo(String screenshotSaveDir, String filename) {
        var videoFullOPath = FMPEG_VIDEO_RECORDER.startRecording(screenshotSaveDir, filename);
        VIDEO_FULL_PATH.set(videoFullOPath);
    }
}
