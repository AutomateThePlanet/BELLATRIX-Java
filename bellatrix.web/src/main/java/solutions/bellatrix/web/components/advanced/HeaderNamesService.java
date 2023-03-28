package solutions.bellatrix.web.components.advanced;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Element;

public class HeaderNamesService {
    private String _xpathToNameElement;
    private Map<Integer, String> _headerNamesIndexes;
    private List<Element> _tableRowHeaders;

    public HeaderNamesService(List<Element> tableHeaderRows, String xpathToNameElement) {
        _tableRowHeaders = tableHeaderRows;
        _xpathToNameElement = xpathToNameElement;
        _headerNamesIndexes = new HashMap<>();
    }

    public HeaderNamesService(List<Element> tableHeaderRows) {
        _tableRowHeaders = tableHeaderRows;
        _xpathToNameElement = "";
        _headerNamesIndexes = new HashMap<>();
    }

    public List<String> ColumnHeaderNames;

    public void setHeaderNamesByIndex(int index, String value) {
        _headerNamesIndexes.put(index, value);
    }

    public List<String> getHeaderNames() {
        initializeHeaderNames();
        return _headerNamesIndexes.values().stream().collect(Collectors.toList());
    }

    public String getNameByPosition(int position) {
        return _headerNamesIndexes.get(position);
    }

//    public <TDto> String getHeaderNameByExpression(Function<TDto, Object> expression) {
//        String propertyName = TypePropertiesNameResolver.getMemberName(expression);
//        PropertyInfo propertyInfo = null;
//        for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(TDto.class)) {
//            if (descriptor.getName().equals(propertyName)) {
//                propertyInfo = new PropertyInfo();
//                propertyInfo.setDisplayName(descriptor.getName());
//                break;
//            }
//        }
//        return getHeaderNameByProperty(propertyInfo);
//    }

    public String getHeaderNameByProperty(Method method) {
        Annotation headerNameAnnotation = method.getAnnotation(HeaderNameAnnotation.class);

        if (headerNameAnnotation == null) {
            return method.getName();
        }

        String headerName = ((HeaderNameAnnotation) headerNameAnnotation).name();
        return headerName;
    }

    public IHeaderInfo getHeaderInfoByProperty(PropertyDescriptor method) {
        Annotation headerNameAnnotation = method.getAnnotation(HeaderNameAnnotation.class);
        String headerName = headerNameAnnotation == null ? method.getName()
                : ((HeaderNameAnnotation) headerNameAnnotation).name();
        int headerOrder = headerNameAnnotation == null ? -1 : ((HeaderNameAnnotation) headerNameAnnotation).order();

        if (headerOrder >= 0) {
            return new HeaderInfo(headerName, headerOrder);
        } else {
            return new HeaderInfo(headerName);
        }
    }

    public Optional<Integer> getHeaderPosition(String header, List<? extends IHeaderInfo> headerInfos, Integer order, boolean throwException) {
        setEmptyHeadersName(headerInfos);

        if (_headerNamesIndexes.entrySet().stream().noneMatch(x -> x.getValue().endsWith(header))) {
            if (throwException) {
                throw new IllegalArgumentException("Header " + header + " was not found.");
            } else {
                return null;
            }
        }

        if (ColumnHeaderNames != null && ColumnHeaderNames.stream().noneMatch(x -> x.endsWith(header))) {
            return null;
        }

        Map<Integer, String> allMatchingHeaders = _headerNamesIndexes.entrySet().stream().filter(x -> x.getValue().endsWith(header))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (allMatchingHeaders.size() > 1) {
            Map<Integer, String> exactMatchHeaders = _headerNamesIndexes.entrySet().stream().filter(x -> x.getValue().equals(header))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (exactMatchHeaders.size() != 1) {
                if (order == null) {
//                    Logger.logWarning("More than one Header with name ending with '" + header + "' was found. Returning the first one.");
                    return allMatchingHeaders.entrySet().stream().findFirst().get().getKey().describeConstable();
                } else {
                    return allMatchingHeaders.entrySet().stream().skip(order).findFirst().get().getKey().describeConstable();
                }
            }

            return exactMatchHeaders.entrySet().stream().findFirst().get().getKey().describeConstable();
        } else if (!allMatchingHeaders.isEmpty()) {
            return allMatchingHeaders.entrySet().stream().findFirst().get().getKey().describeConstable();
        }

        return null;
    }

    private void setEmptyHeadersName(List<? extends IHeaderInfo> headerInfos) {
        List<String> headerNameCollection = getHeaderNames();
        int count = Math.min(headerInfos.size(), headerNameCollection.size());

        for (int i = 0; i < count; i++) {
            String headerName = headerInfos.get(i).getHeaderName();
            String collectionName = headerNameCollection.get(i);

            if (collectionName != headerName && (collectionName == null || collectionName.trim().isEmpty())) {
                setHeaderNamesByIndex(i, headerName);
            }
        }
    }

    private void initializeHeaderNames() {
        if (_headerNamesIndexes != null) {
            return;
        }

        _headerNamesIndexes = new HashMap<>();
        Map<Integer, HeaderRowIndex> rowSpanPairs = new HashMap<>();
        int rowIndex = 0;
        for (Element tableRowHeader : _tableRowHeaders) {
            int columnIndex = 0;
            int headerCellsCount = tableRowHeader.select("th").size();

            for (Element currentHeader : tableRowHeader.select("th")) {
                String headerName;
                while (rowSpanPairs.containsKey(columnIndex) && rowIndex > rowSpanPairs.get(columnIndex).getRowIndex()) {
                    headerName = rowSpanPairs.get(columnIndex).getHeaderName();
                    rowSpanPairs.get(columnIndex).setRowspan(rowSpanPairs.get(columnIndex).getRowspan() - 1);
                    int currentColSpan = rowSpanPairs.get(columnIndex).getColspan();

                    if (rowSpanPairs.get(columnIndex).getRowspan() == 1) {
                        rowSpanPairs.remove(columnIndex);
                    }

                    columnIndex++;
                }

                if (_xpathToNameElement == null || _xpathToNameElement.isEmpty()) {
                    headerName = currentHeader.text();
                } else {
                    headerName = StringEscapeUtils.unescapeHtml4(currentHeader.selectFirst(_xpathToNameElement).text());
                }

                int colSpan = getColSpan(currentHeader);
                int rowSpan = getRowSpan(currentHeader);

                if (rowSpan > 1) {
                    rowSpanPairs.put(columnIndex, new HeaderRowIndex(headerName, rowSpan, colSpan, rowIndex));
                }

                addColumnIndex(colSpan, new AtomicInteger(columnIndex), headerName);
            }

            rowIndex++;
        }
    }

    private void addColumnIndex(int colSpan, AtomicInteger columnIndex, String headerName) {
        if (colSpan == 0) {
            addHeaderNameIndex(columnIndex.getAndIncrement(), columnIndex, headerName);
        } else {
            int initialIndex = columnIndex.get();
            for (int i = 0; i < colSpan; i++) {
                addHeaderNameIndex(initialIndex + i, columnIndex, headerName);
            }
            columnIndex.addAndGet(colSpan - 1);
        }
    }

    private void addHeaderNameIndex(int operationalIndex, AtomicInteger columnIndex, String headerName) {
        if (_headerNamesIndexes.containsKey(operationalIndex) && _headerNamesIndexes.get(operationalIndex).equals(headerName)) {
            return;
        }

        if (_headerNamesIndexes.containsKey(operationalIndex)) {
            _headerNamesIndexes.put(operationalIndex, _headerNamesIndexes.get(operationalIndex) + " " + headerName);
        } else {
            _headerNamesIndexes.put(operationalIndex, headerName);
        }
    }

    private int getColSpan(Element headerCell) {
        String colSpanText = headerCell.attr("colspan");
        return colSpanText.isEmpty() ? 0 : Integer.parseInt(colSpanText);
    }

    private int getRowSpan(Element headerCell) {
        String rowSpanText = headerCell.attr("rowspan");

        return rowSpanText.isEmpty() ? 0 : Integer.parseInt(rowSpanText);
    }
}

