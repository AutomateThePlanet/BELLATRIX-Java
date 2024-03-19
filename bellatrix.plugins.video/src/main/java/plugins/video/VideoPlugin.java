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

import org.apache.commons.io.FileUtils;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;

import java.io.File;
import java.lang.reflect.Method;

public abstract class VideoPlugin extends Plugin {
    public static final EventListener<VideoPluginEventArgs> VIDEO_GENERATED = new EventListener<>();
    private static final FFmpegVideoRecorder FMPEG_VIDEO_RECORDER = new FFmpegVideoRecorder();
    private static final ThreadLocal<String> VIDEO_FULL_PATH = new ThreadLocal<>();
    private final boolean isEnabled;

    public VideoPlugin(boolean isEnabled) {
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

    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
        if (isEnabled) {
            FMPEG_VIDEO_RECORDER.close();
            if (testResult == TestResult.FAILURE) {
                VIDEO_GENERATED.broadcast(new VideoPluginEventArgs(VIDEO_FULL_PATH.get()));
            } else {
                try {
                    FileUtils.forceDeleteOnExit(new File(VIDEO_FULL_PATH.get()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    protected void takeVideo(String screenshotSaveDir, String filename) {
        var videoFullPath = FMPEG_VIDEO_RECORDER.startRecording(screenshotSaveDir, filename);
        VIDEO_FULL_PATH.set(videoFullPath);
    }
}
