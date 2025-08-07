# ObjectConverter

The `ObjectConverter` is an interface that defines methods for converting between string and Java objects. The Bellatrix Data Module includes an out-of-the-box implementation for JSON conversion, but you can implement your own converter for other formats like XML.

## JsonConverter Implementation

The `JsonConverter` class provides a simple way to convert between Java objects and their JSON string representations using the Gson library. It offers flexible configuration through a `Consumer<GsonBuilder>` parameter in its constructor.

**Key Features:**

- Converts objects to JSON strings with `toString()` methods
- Deserializes JSON strings back to Java objects with `fromString()`
- Supports list deserialization with `fromStringToList()`
- Includes error handling for null values and malformed JSON
- Provided default settings which can be seen in `getInstance()` method
- Mechanism for extending or overriding default settings via constructor

**Configuration Process:**

1. The constructor accepts a `Consumer<GsonBuilder>` that allows you to customize serialization settings
2. The `getInstance` method creates a `Gson` instance with predefined default settings
3. Your custom consumer function then overrides these defaults with your specific configuration
4. The final configured `Gson` instance handles all serialization and deserialization operations

### Implementing Custom Converters

**Custom Configuration Example:**

If your server returns JSON data in a different format than the default (e.g., using UpperCamelCase instead of lowercase_with_underscores), you can configure the `JsonConverter` to handle this:

```json
{
    "ArtistId": 281,
    "Name": "Artist Name"
}
```

### Option 1: Repository-specific Configuration

For cases where you need to handle a unique format, configure the `JsonConverter` directly in your repository class:

```java
protected ArtistRepository() {
    super(Artist.class, new JsonConverter(builder -> {
        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
    }), () -> {
        var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
        var httpContext = new HttpContext(httpSettings);
        httpContext.addPathParameter("/artists");
        return httpContext;
    });
}
```

This configuration tells the `JsonConverter` to use UpperCamelCase field naming, allowing it to properly map JSON fields like "ArtistId" and "Name" to your Java object properties.

### Option 2: Custom Converter Class

Create your own custom converter by extending the `JsonConverter` class. This is useful when you have specific rules applicable to multiple entities of the same type: The benefit of this approach is that you can encapsulate the conversion logic in a single class, making it reusable across different repositories as well as providing multiple options at single place.

```java
public class ArtistsConverter extends JsonConverter {
    public ArtistsConverter() {
        super(builder -> {
            builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        });
    }
}
```

Recommended for cases where you have multiple repositories that need to handle the same JSON format. This way, you can reuse the converter across different repositories without duplicating code.

Then use it in your repository:

```java
public ArtistRepository() {
    super(Artist.class, new ArtistsConverter(), () -> {
        var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
        var httpContext = new HttpContext(httpSettings);
        httpContext.addPathParameter("/artists");
        return httpContext;
    });
}
```