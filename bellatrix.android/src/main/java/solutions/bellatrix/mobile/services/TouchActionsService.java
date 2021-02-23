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

package solutions.bellatrix.mobile.services;

import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import solutions.bellatrix.mobile.components.AndroidComponent;
import solutions.bellatrix.mobile.infrastructure.DriverService;

import java.time.Duration;

public class TouchActionsService extends MobileService {
    private final MultiTouchAction wrappedMultiAction = new MultiTouchAction(DriverService.getWrappedAndroidDriver());

    public <TComponent extends AndroidComponent> TouchActionsService tap(TComponent component, int count) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.tap(TapOptions.tapOptions()
                .withPosition(PointOption.point(component.getLocation().getX(), component.getLocation().getY())).withTapsCount(count));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public <TComponent extends AndroidComponent> TouchActionsService press(TComponent component) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.press(PointOption.point(component.getLocation().getX(), component.getLocation().getY()));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public TouchActionsService press(Integer x, Integer y) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.press(PointOption.point(x, y));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public <TComponent extends AndroidComponent> TouchActionsService longPress(TComponent component) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.longPress(PointOption.point(component.getLocation().getX(), component.getLocation().getY()));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public TouchActionsService longPress(Integer x, Integer y) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.longPress(PointOption.point(x, y));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public TouchActionsService wait(Long waitTimeMilliseconds) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.waitAction(WaitOptions
                .waitOptions(Duration.ofMillis(waitTimeMilliseconds)));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public <TComponent extends AndroidComponent> TouchActionsService moveTo(TComponent component) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.moveTo(PointOption.point(component.getLocation().getX(), component.getLocation().getY()));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public TouchActionsService moveTo(Integer x, Integer y) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.moveTo(PointOption.point(x, y));
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public TouchActionsService release() {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction.release();
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public <TComponent extends AndroidComponent> TouchActionsService swipe(TComponent firstComponent, TComponent secondComponent, int duration) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction
                .press(PointOption.point(firstComponent.getLocation().getX(), firstComponent.getLocation().getY()))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                .moveTo(PointOption.point(secondComponent.getLocation().getX(), secondComponent.getLocation().getY()))
                .release().perform();
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public TouchActionsService swipe(int startx, int starty, int endx, int endy, int duration) {
        TouchAction touchAction = new TouchAction(DriverService.getWrappedAndroidDriver());
        touchAction
                .press(PointOption.point(startx, starty))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                .moveTo(PointOption.point(endx, endy))
                .release().perform();
        wrappedMultiAction.add(touchAction);

        return this;
    }

    public void perform() {
        wrappedMultiAction.perform();
    }
}
