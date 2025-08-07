# Bellatrix Data Module

## Overview

The **Bellatrix Data Module** simplifies data management in your tests by providing a clean, type-safe interface for API interactions.

## Who Is This Guide For?

Test automation engineers looking to streamline data management in their automation projects.

## Why Use the Data Module?

### Simplicity

> Eliminate boilerplate HTTP request code and focus on your test logic.

**Traditional Approach:**
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:3001/api/artists"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(artist)))
    .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
Artist createdArtist = gson.fromJson(response.body(), Artist.class);
```

**Bellatrix Data Module Approach:**

```java
RepositoryFactory.INSTANCE.registerRepository(Artist.class, ArtistRepository.class);
Artist artist = Artist.builder().name("Artist Name").build().create();
```

### Speed
> [!NOTE]\
> Start writing tests immediately—no need to learn complex HTTP client libraries.

**Traditional Approach:**
Requires creating an HTTP client, building a request, and then executing it with the client:

``` java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:3001/api/artists"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(artist)))
    .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
```

**With Bellatrix Approach:**
Just define your HTTP entity and create a repository instance - no boilerplate required.

## Core Components

### HttpEntity

HttpEntity represents the data objects your API works with. Each entity corresponds to a resource (such as Artist, Album, etc.) and defines the structure and fields of that resource.

**Key Features:**
- **Type Safety**: Generic type parameters ensure compile-time type checking
- **JSON Serialization**: Built-in support for JSON field mapping with `@SerializedName`
- **Builder Pattern**: Lombok's `@SuperBuilder` enables fluent object creation

```java
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Artist extends HttpEntity<String, Artist> {
    @SerializedName("ArtistId")
    private String id;

    @SerializedName("Name")
    private String name;

    @Override
    public String getIdentifier() {
        return id;
    }
}
```

### HttpRepository

HttpRepository acts as the client for performing CRUD operations on your entities. It manages HTTP requests and responses, providing a clean interface for data interactions.

**Key Features:**
- **Generic CRUD Operations**: Built-in methods for Create, Read, Update, Delete
- **Configurable Conversion**: Custom JSON converters for different API formats
- **HTTP Context Management**: Flexible configuration for different endpoints

```java
public class ArtistRepository extends HttpRepository<String, Artist> {
    public ArtistRepository() {
        super(Artist.class, new JsonConverter(builder -> {
            builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        }), () -> {
            var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
            var httpContext = new HttpContext(httpSettings);
            httpContext.addPathParameter("/artists");
            return httpContext;
        });
    }
}
```

### Usage Example

Once you've defined your entity and repository, you can start performing operations immediately:

```java
// Create repository instance
ArtistRepository artistRepository = new ArtistRepository();

// Perform CRUD operations
List<Artist> artists = artistRepository.getAll();
Artist newArtist = Artist.builder().name("New Artist").build();
Artist created = artistRepository.create(newArtist);
```

## Extensibility in mind

The Bellatrix Data Module is designed with extensibility in mind. Explore these guides to learn more:

### Core Concepts
- **[Create Repository](src/test/java/04_create_repository/ReadMe.md)** - Learn how to create custom repositories for your data entities
- **[HTTP Context Configuration](src/test/java/05_http_context/ReadMe.md)** - Configure HTTP settings and customize requests for your API endpoints
- **[Object Converter](src/test/java/06_object_converter/ReadMe.md)** - Implement custom converters for different data formats (JSON, XML, etc.)

### Additional Resources
- **[Project Setup](src/test/java/01_project_setup/)** - Initial project configuration
- **[HTTP Settings](src/test/java/02_http_settings/)** - Configure HTTP client settings
- **[Create Entity](src/test/java/03_create_entity/)** - Define your data entities