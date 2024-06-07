/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plugins.jira.zephyr.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ZephyrTestCycle {
    public ZephyrTestCycle(String projectKey, String name, String statusName) {
        this.projectKey = projectKey;
        this.name = name;
        this.statusName = statusName;
    }

    private String id;
    private String key;
    private String projectKey;
    private String name;
    private String statusName;
    private String plannedStartDate;
    private String plannedEndDate;
}
