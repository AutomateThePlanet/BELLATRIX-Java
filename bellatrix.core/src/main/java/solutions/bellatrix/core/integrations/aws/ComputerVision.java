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
package solutions.bellatrix.core.integrations.aws;

import org.testng.Assert;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;
import solutions.bellatrix.core.utilities.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComputerVision {
    private TextractClient textractClient;
    Region region;

    public ComputerVision() {
        this(Region.US_EAST_2);
    }

    public ComputerVision(Region region) {
        // initialize later from configuration
        this.region = region;
    }

    public void validateText(String localFile, List<String> expectedTextSnippets) {
        var actualTextSnippets = extractBlocksFromLocalFile(localFile);

        Assert.assertTrue(actualTextSnippets.containsAll(expectedTextSnippets) , "Some of the expected text snippets weren't present on the actual PDF/Image.");
    }

    public void validateFormLayout(String expectedLayoutFilePath, String fileToBeValidatedPath, double delta) {
        var expectedAnalyzedFile = extractBlocksFromLocalFile(expectedLayoutFilePath);
        var actualAnalyzedFile = extractBlocksFromLocalFile(fileToBeValidatedPath);

        int currentWordIndex = 0;
        for (var currentExpectedWord:expectedAnalyzedFile) {
            var currentActualWord = actualAnalyzedFile.get(currentWordIndex++);

            Log.info("Its bounding box are:");
            Log.info("E: Upper left => X: %f, Y= %f", currentExpectedWord.geometry().polygon().get(0).x(), currentExpectedWord.geometry().polygon().get(0).y());
            Log.info("E: Upper right => X: %f, Y= %f", currentExpectedWord.geometry().polygon().get(1).x(), currentExpectedWord.geometry().polygon().get(1).y());
            Log.info("E: Lower right => X: %f, Y= %f", currentExpectedWord.geometry().polygon().get(2).x(), currentExpectedWord.geometry().polygon().get(2).y());
            Log.info("E: Lower left => X: %f, Y= %f", currentExpectedWord.geometry().polygon().get(3).x(), currentExpectedWord.geometry().polygon().get(3).y());

            Log.info("A: Upper left => X: %f, Y= %f", currentActualWord.geometry().polygon().get(0).x(), currentActualWord.geometry().polygon().get(0).y());
            Log.info("A: Upper right => X: %f, Y= %f", currentActualWord.geometry().polygon().get(1).x(), currentActualWord.geometry().polygon().get(1).y());
            Log.info("A: Lower right => X: %f, Y= %f", currentActualWord.geometry().polygon().get(2).x(), currentActualWord.geometry().polygon().get(2).y());
            Log.info("A: Lower left => X: %f, Y= %f", currentActualWord.geometry().polygon().get(3).x(), currentActualWord.geometry().polygon().get(3).y());

            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(0).x(), currentActualWord.geometry().polygon().get(0).x(), delta);
            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(0).y(), currentActualWord.geometry().polygon().get(0).y(), delta);

            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(1).x(), currentActualWord.geometry().polygon().get(1).x(), delta);
            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(1).y(), currentActualWord.geometry().polygon().get(1).y(), delta);

            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(2).x(), currentActualWord.geometry().polygon().get(2).x(), delta);
            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(2).y(), currentActualWord.geometry().polygon().get(2).y(), delta);

            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(3).x(), currentActualWord.geometry().polygon().get(3).x(), delta);
            Assert.assertEquals(currentExpectedWord.geometry().polygon().get(3).y(), currentActualWord.geometry().polygon().get(3).y(), delta);
        }
    }

    public List<String> extractOCRTextFromLocalFile(String localFile) {
        List<String> textSnippets = extractBlocksFromLocalFile(localFile).stream().map(b -> b.text()).collect(Collectors.toList());
        return textSnippets;
    }

    public List<Block> extractBlocksFromLocalFile(String localFile) {
        TextractClient textractClient = TextractClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
        List<Block> blocks = new ArrayList<>();
        try(textractClient) {
            InputStream sourceStream = new FileInputStream(localFile);
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

            Document myDoc = Document.builder()
                    .bytes(sourceBytes)
                    .build();

            DetectDocumentTextRequest detectDocumentTextRequest = DetectDocumentTextRequest.builder()
                    .document(myDoc)
                    .build();

            DetectDocumentTextResponse textResponse = textractClient.detectDocumentText(detectDocumentTextRequest);
            blocks = textResponse.blocks();

        } catch (TextractException | FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return blocks;
    }

}
