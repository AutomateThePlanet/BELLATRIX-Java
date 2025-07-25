package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import solutions.bellatrix.core.utilities.Log;

import java.util.ArrayList;
import java.util.List;

public class CurlLoggingFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        Log.info("====== CURL COMMAND ======");
        List<String> generatedCurl = generateCurl(requestSpec);
        logCurl(generatedCurl);
        return response;
    }

    private List<String> generateCurl(FilterableRequestSpecification requestSpec) {
        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();
        List<Header> headers = requestSpec.getHeaders().asList();
        String body = requestSpec.getBody() != null ? requestSpec.getBody().toString() : null;

        List<String> curlParts = new ArrayList<>();

        curlParts.add("curl --location '%s' \\".formatted(uri));
        curlParts.add("-X " + method);

        for (Header header : headers) {
            curlParts.add("--header '%s: %s' \\".formatted(header.getName(), header.getValue()));
        }
        if (body != null && !body.isEmpty()) {
            curlParts.add("--data '" + body.replace("'", "'\"'\"'") + "'");
        }
        return curlParts;
    }

    private void logCurl(List<String> generatedCurl) {
        for (String curlPart : generatedCurl) {
            Log.info(curlPart);
        }
    }
}