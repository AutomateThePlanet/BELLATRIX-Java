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

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageReader;
import solutions.bellatrix.core.plugins.EventListener;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class SmsListener {
    public static EventListener<SmsEventArgs> MESSAGE_RECEIVED = new EventListener<>();

    private String phoneNumber;
    private final List<Message> messages = new ArrayList<>();
    private ScheduledExecutorService scheduler;
    private Instant start;

    public SmsListener(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SmsListener() {
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public Message getMessage(Predicate<Message> condition) {
        return messages.stream().filter(condition).findFirst().orElse(null);
    }

    public Message getFirstMessage() {
        return messages.get(0);
    }

    public Message getLastMessage() {
        return messages.get(messages.size() - 1);
    }

    public Message getLastMessage(Predicate<Message> condition) {
        var foundMessages = messages.stream().filter(condition).toList();
        return foundMessages.get(foundMessages.size() - 1);
    }

    public void listen() {
        start = Instant.now();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduler.schedule(this::checkForMessages, 0, TimeUnit.MILLISECONDS);
    }

    public void stopListening() {
        if (this.scheduler != null) {
            this.scheduler.shutdownNow();
            this.scheduler = null;
        }
    }

    private void checkForMessages() {
        var messageReader = new MessageReader()
                .setDateSentAfter(ZonedDateTime.ofInstant(start, ZoneId.of("UTC")));
        if (phoneNumber != null && !phoneNumber.isBlank()) messageReader.setFrom(phoneNumber);

        var foundMessages = messageReader.read();

        while (foundMessages.iterator().hasNext()) {
            var message = foundMessages.iterator().next();
            messages.add(message);
            MESSAGE_RECEIVED.broadcast(new SmsEventArgs(this, message));
        }

        start = Instant.now();
    }
}
