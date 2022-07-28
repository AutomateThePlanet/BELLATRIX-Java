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
package solutions.bellatrix.core.utilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.testng.Assert;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSVService {
    public static void validateData(String localFile, List<String[]> expectedColumns) throws Exception {
        var actualColumns = readAllLines(localFile);
        Assert.assertEquals(expectedColumns.size(), actualColumns.size());
        for (int i = 0; i < expectedColumns.size(); i++) {
            for (int j = 0; j < expectedColumns.get(i).length; j++) {
                Assert.assertEquals(expectedColumns.get(i)[j], actualColumns.get(i)[j]);
            }
        }
    }

    // https://www.baeldung.com/opencsv
    public static List<String[]> readAllLines(String filePath) throws Exception {
        try (Reader inputStreamReader = Files.newBufferedReader(Path.of(filePath))) {
            CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    // Skip the header
                    .withSkipLines(1)
                    .build();
            try (csvReader) {
                return csvReader.readAll();
            }
        }
    }
}