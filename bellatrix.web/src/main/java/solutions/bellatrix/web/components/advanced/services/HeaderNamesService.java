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

package solutions.bellatrix.web.components.advanced.services;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Element;
import solutions.bellatrix.core.utilities.*;
import solutions.bellatrix.web.components.advanced.HeaderInfo;
import solutions.bellatrix.web.components.advanced.HeaderRowIndex;
import solutions.bellatrix.web.components.advanced.TableHeader;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public class HeaderNamesService {
    private String xpathToNameElement;
    private Map<Integer, String> headerNamesIndexes;
    private List<Element> tableRowHeaders;

    public HeaderNamesService(List<Element> tableRowHeaders) {
        this.tableRowHeaders = tableRowHeaders;
    }

    public HeaderNamesService(List<Element> tableRowHeaders, String xpathToNameElement) {
        this(tableRowHeaders);
        this.xpathToNameElement = xpathToNameElement;
    }

    public TableLocators locators() {
        return SingletonFactory.getInstance(TableLocators.class);
    }

    @Getter @Setter private List<String> columnHeaderNames;

    public void setHeaderNamesByIndex(int index, String value) {
        headerNamesIndexes.put(index, value);
    }
    public List<String> getHeaderNames() {
        initializeHeaderNames();

        return headerNamesIndexes.values().stream().toList();
    }

    public String getNameByPosition(int position) {
        return headerNamesIndexes.get(position);
    }

    public Integer getHeaderPosition(String header, List<? extends HeaderInfo> headerInfos) {
        return  getHeaderPosition(header, headerInfos, null, null);
    }

    public Integer getHeaderPosition(String header, List<? extends HeaderInfo> headerInfos, Integer order) {
        return getHeaderPosition(header, headerInfos, order, null);
    }

    public Integer getHeaderPosition(String header, List<? extends HeaderInfo> headerInfos, Integer order, Boolean throwException) {
        setEmptyHeadersName(headerInfos);

        if (headerNamesIndexes.values().stream().noneMatch(x -> x.endsWith(header))) {
            if (throwException != null && throwException) {
                throw new IllegalArgumentException(String.format("Header %s was not found.", header));
            } else return null;
        }

        if (columnHeaderNames != null && columnHeaderNames.stream().noneMatch(x -> x.endsWith(header))) {
            return null;
        }

        var allMatchingHeaders = headerNamesIndexes.entrySet().stream()
                .filter(entry -> entry.getValue().endsWith(header))
                .toList();

        if (allMatchingHeaders.size() > 1) {
            var exactMatchHeaders = headerNamesIndexes.entrySet().stream()
                    .filter(entry -> Objects.equals(entry.getValue(), header))
                    .toList();

            if (exactMatchHeaders.size() != 1) {
                if (order == null) {
                    DebugInformation.debugInfo(String.format("More than one Header with name ending with '%s' was found. Returning the first one.", header));
                    return allMatchingHeaders.get(0).getKey();
                } else {
                    return allMatchingHeaders.get(order).getKey();
                }
            }

            return exactMatchHeaders.get(0).getKey();
        } else if (!allMatchingHeaders.isEmpty()) {
            return allMatchingHeaders.get(0).getKey();
        }

        return null;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> String getHeaderNameByExpression(Class<T> dtoClass, Predicate<T> expression) {
        T dtoInstance = InstanceFactory.create(dtoClass);

        for (Field field : dtoClass.getFields()) {
            field.setAccessible(true);

            Object fieldValue = field.get(dtoInstance);

            if (expression.test((T)fieldValue)) {
                return getHeaderNameByField(field);
            }
        }

        return null;
    }

    public String getHeaderNameByField(Field field) {
        if (field.isAnnotationPresent(TableHeader.class)) {
            return field.getAnnotation(TableHeader.class).name();
        } else {
            return field.getName();
        }
    }

    public Integer getHeaderOrderByField(Field field) {
        if (field.isAnnotationPresent(TableHeader.class)) {
            return field.getAnnotation(TableHeader.class).order();
        } else return null;
    }

    public HeaderInfo getHeaderInfoByField(Field field) {
        var headerName = getHeaderNameByField(field);
        var headerOrder = getHeaderOrderByField(field) != null ? getHeaderOrderByField(field) : null;

        if (headerOrder == null) return new HeaderInfo(headerName);

        else return new HeaderInfo(headerName, headerOrder);
    }

    private void setEmptyHeadersName(List<? extends HeaderInfo> headerInfos) {
        var headerNameCollection = getHeaderNames();
        var count = Math.min(headerInfos.size(), headerNameCollection.size());

        for (int i = 0; i < count; i++) {
            var headerName = headerInfos.get(i).getHeaderName();
            var collectionName = headerNameCollection.get(i);

            if (!Objects.equals(collectionName, headerName) && (collectionName == null || collectionName.isBlank())) {
                setHeaderNamesByIndex(i, headerName);
            }
        }
    }

    private void initializeHeaderNames() {
        if (headerNamesIndexes != null && !headerNamesIndexes.isEmpty()) {
            return;
        }

        headerNamesIndexes = new HashMap<>();
        var rowSpanPairs = new HashMap<Integer, HeaderRowIndex>();
        int rowIndex = 0;
        for (var tableRowHeader : tableRowHeaders) {
            Ref<Integer> columnIndex = new Ref<>(0);
            var headerCellsCount = tableRowHeader.selectXpath(locators().getHeaderXpath()).size();

            for (var currentHeader : tableRowHeader.selectXpath(locators().getHeaderXpath())) {
                String headerName;
                while (rowSpanPairs.containsKey(columnIndex.value) && rowIndex > rowSpanPairs.get(columnIndex.value).getRowIndex()) {
                    headerName = rowSpanPairs.get(columnIndex.value).getHeaderName();
                    rowSpanPairs.get(columnIndex.value).setRowspan(rowSpanPairs.get(columnIndex.value).getRowspan() -1);
                    // UNUSED, REMOVE ME: int currentColSpan = rowSpanPairs.get(columnIndex.value).getColspan();

                    if (rowSpanPairs.get(columnIndex.value).getRowspan() == 1) {
                        rowSpanPairs.remove(columnIndex.value);
                    }

                    columnIndex.value++;
                }

                if (xpathToNameElement == null || xpathToNameElement.isBlank()) {
                    headerName = currentHeader.text();
                } else {
                    headerName = StringEscapeUtils.unescapeHtml4(currentHeader.selectXpath(xpathToNameElement).text());
                }

                int colSpan = getColSpan(currentHeader);
                int rowSpan = getRowSpan(currentHeader);

                if (rowSpan > 1) {
                    rowSpanPairs.put(columnIndex.value, new HeaderRowIndex(headerName, rowSpan, colSpan, rowIndex));
                }

                addColumnIndex(colSpan, columnIndex, headerName);
            }

            rowIndex++;
        }
    }

    private void addColumnIndex(int colSpan, Ref<Integer> columnIndex, String headerName) {
        if (colSpan == 0) {
            addHeaderNameIndex(columnIndex.value, headerName);
            columnIndex.value++;
        } else {
            int initialIndex = columnIndex.value;
            for (int i = 0; i < colSpan; i++) {
                addHeaderNameIndex(initialIndex + i, headerName);
                columnIndex.value++;
            }
        }
    }

    private void addHeaderNameIndex(int operationalIndex, String headerName) {
        if (headerNamesIndexes.containsKey(operationalIndex) && headerNamesIndexes.get(operationalIndex) == headerName) {
            return;
        }

        if (headerNamesIndexes.containsKey(operationalIndex)) {
            headerNamesIndexes.put(operationalIndex, headerNamesIndexes.get(operationalIndex) + " " + headerName);
        } else {
            headerNamesIndexes.put(operationalIndex, headerName);
        }
    }

    private int getColSpan(Element headerCell) {
        return HtmlService.getAttribute(headerCell, "colspan", Integer.class);
    }

    private int getRowSpan(Element headerCell) {
        return HtmlService.getAttribute(headerCell, "rowspan", Integer.class);
    }
}
