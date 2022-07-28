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

package checkoutpage;

import lombok.Getter;
import lombok.Setter;

// TODO: can be converted to record?
public class PurchaseInfo {
    @Getter @Setter private String firstName;
    @Getter @Setter private String lastName;
    @Getter @Setter private String company;
    @Getter @Setter private String country;
    @Getter @Setter private String address1;
    @Getter @Setter private String address2;
    @Getter @Setter private String city;
    @Getter @Setter private String zip;
    @Getter @Setter private String phone;
    @Getter @Setter private String email;
    @Getter @Setter private Boolean shouldCreateAccount = false;
    @Getter @Setter private Boolean shouldCheckPayment = false;
}
