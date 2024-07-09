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

package solutions.bellatrix.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.experimental.UtilityClass;
import solutions.bellatrix.core.configuration.ConfigurationService;

import java.util.List;

@UtilityClass
public class SmsService {
    private static final TwilioSettings settings = ConfigurationService.get(TwilioSettings.class);

    static {
        Twilio.init(settings.getAccountSID(), settings.getAuthToken());
    }

    public static SmsListener listenForSms(String fromNumber) {
        var smsListener = new SmsListener(fromNumber);
        smsListener.listen();
        return smsListener;
    }

    public static SmsListener listenForSms() {
        var smsListener = new SmsListener();
        smsListener.listen();
        return smsListener;
    }

    public static void stopListeningForSms(SmsListener smsListener) {
        smsListener.stopListening();
    }

    public static List<Message> getMessages(SmsListener smsListener) {
        return smsListener.getMessages();
    }

    public static Message getFirstMessage(SmsListener smsListener) {
        return smsListener.getFirstMessage();
    }

    public static Message getLastMessage(SmsListener smsListener) {
        return smsListener.getLastMessage();
    }
}
