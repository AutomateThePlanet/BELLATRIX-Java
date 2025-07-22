package data.repositories;

import solutions.bellatrix.data.http.contracts.ObjectConverter;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;
import solutions.bellatrix.data.http.infrastructure.HttpRepository;

import java.util.function.Supplier;

public abstract class ServiceNowRepository<T extends HttpEntity> extends HttpRepository<T> {
    protected ServiceNowRepository(Class<T> type, ObjectConverter serializer, Supplier<HttpContext> context) {
        super(type, serializer, context);
    }
}