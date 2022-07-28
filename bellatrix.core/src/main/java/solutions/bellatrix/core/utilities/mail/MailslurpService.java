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
package solutions.bellatrix.core.utilities.mail;

import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.apis.WaitForControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.clients.Configuration;
import com.mailslurp.models.Email;
import com.mailslurp.models.InboxDto;
import com.mailslurp.models.SendEmailOptions;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import solutions.bellatrix.core.utilities.ResourcesReader;
import solutions.bellatrix.core.utilities.TimestampBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MailslurpService {
    private final Long TIMEOUT = 30000L;
    private ApiClient defaultClient;
    private InboxControllerApi inboxControllerApi;
    private String apiKey;

    public MailslurpService(String apiKey) {
        this.apiKey = apiKey;

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        defaultClient = Configuration.getDefaultApiClient();

        // IMPORTANT set api client timeouts
        defaultClient.setConnectTimeout(TIMEOUT.intValue());
        defaultClient.setWriteTimeout(TIMEOUT.intValue());
        defaultClient.setReadTimeout(TIMEOUT.intValue());

        // IMPORTANT set API KEY and client
        defaultClient.setHttpClient(httpClient);
        defaultClient.setApiKey(apiKey);

        inboxControllerApi = new InboxControllerApi(defaultClient);
    }

    @SneakyThrows
    public InboxDto createInbox(String name) {
        InboxDto inbox = inboxControllerApi.createInbox(
                null,
                Arrays.asList(),
                name,
                "description_example",
                true,
                false,
                null,
                600000L,
                false,
                String.valueOf(InboxDto.InboxTypeEnum.HTTP_INBOX),
                false);

        return inbox;
    }

    @SneakyThrows
    public Email waitForLatestEmail(InboxDto inbox, OffsetDateTime since) {
        var waitForControllerApi = new WaitForControllerApi(defaultClient);
        Email receivedEmail = waitForControllerApi
                .waitForLatestEmail(inbox.getId(), TIMEOUT, false, null, since, null, 10000L);

        return receivedEmail;
    }

    public void sendEmail(InboxDto inbox, String toEmail, String subject, String templateName) throws ApiException {
        var emailBody = ResourcesReader.getFileAsString(MailslurpService.class, templateName);
        // send HTML body email
        SendEmailOptions sendEmailOptions = new SendEmailOptions()
                .to(Collections.singletonList(toEmail))
                .subject(subject)
                .body(emailBody);

        inboxControllerApi.sendEmail(inbox.getId(), sendEmailOptions);
    }

    @SneakyThrows
    public String loadEmailBody(WebDriver driver, String htmlBody, boolean cloudExecuted) {
        htmlBody = htmlBody.replace("\n", "").replace("\\/", "/").replace("\\\"", "\"");
        String fileName = String.format("%s.html", TimestampBuilder.getGuid());
        var file = writeStringToTempFile(htmlBody);
        driver.get(file.toPath().toUri().toString());

        if (cloudExecuted) {
            driver.get("http://local-folder.lambdatest.com/" + fileName);
        } else {
            driver.get(file.toPath().toUri().toString());
        }

        return htmlBody;
    }

    private File writeStringToTempFile(String fileContent) throws IOException {
        Path tempFile = Files.createTempFile(null, ".html");
        try (var bw = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            bw.write(fileContent);
        }
        return tempFile.toFile();
    }
}