package solutions.bellatrix.data.http.infrastructure;

import lombok.Getter;

@Getter
public class HttpHeader {
    private final String name;
    private final String value;

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        HttpHeader ob = (HttpHeader)obj;
        return this.value.equals(ob.getValue());
    }
}