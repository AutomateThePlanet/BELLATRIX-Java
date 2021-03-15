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

package solutions.bellatrix.ios.components;

import solutions.bellatrix.ios.components.contracts.ComponentDisabled;
import solutions.bellatrix.ios.services.TouchActionsService;
import solutions.bellatrix.core.plugins.EventListener;

public class SeekBar extends IOSComponent implements ComponentDisabled {
    public final static EventListener<ComponentActionEventArgs> SETTING_PERCENTAGE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> PERCENTAGE_SET = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public void set(Number percentage) {
        SETTING_PERCENTAGE.broadcast(new ComponentActionEventArgs(this, percentage.toString()));

        int end = findElement().getSize().getWidth();
        int y = findElement().getLocation().getY();
        var touchActionsService = new TouchActionsService();
        int moveTo = (int)(((double)percentage / 100) * end);
        touchActionsService.press(moveTo, y).release().perform();

        PERCENTAGE_SET.broadcast(new ComponentActionEventArgs(this, percentage.toString()));
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }
}
