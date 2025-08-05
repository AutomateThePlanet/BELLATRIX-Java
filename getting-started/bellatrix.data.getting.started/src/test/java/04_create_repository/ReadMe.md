# HTTP Repositories

## Overview

The `HttpRepository` is the base class for all repositories that interact with your data source via HTTP calls. It provides methods to perform CRUD operations on your data entities.

## Key Features

- **Type Safety**: Generic `<THttpEntity extends HttpEntity>` allows you to define the specific entity type the repository handles, ensuring type safety and consistency across your data operations
- **Consistent Interface**: Provides a uniform interface for data operations across different repositories
- **HTTP Management**: Handles HTTP-specific details like request context and response handling automatically

## How It Works

The `HttpRepository` uses three core components to manage data operations:

- **repositoryContext** (`HttpContext`): The base context for the repository, initialized in the constructor with settings specific to the data source, such as base path and resource-specific settings
- **requestContext** (`HttpContext`): The context for the current request, which can include headers, query parameters, and other request-specific settings
- **objectConverter** (`ObjectConverter`): An interface that defines methods for converting between strings and Java objects. There is an out-of-the-box implementation for JSON conversion, but you can implement your own converter for other formats (e.g., XML)

## Request Flow

1. When the repository is created, `repositoryContext` stores the base context
2. For each request, `requestContext` handles request-specific parameters like headers and query parameters
3. When a response with data is received, `objectConverter` converts the response data into Java objects
4. At the end of the request, the request context is reset to the repository context

This design enables reusing the repository context as a foundation for all requests while allowing request-specific customization. This approach maintains a single repository instance for all operations, with no overlap between different requests, ensuring that each request can be tailored to its specific needs without affecting others.

## Example: Creating an Artist Repository

To demonstrate how to use the `HttpRepository`, let's create a complete example with an `Artist` entity and its corresponding repository.

### Step 1: Create the Entity Class

```java
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Artist extends HttpEntity<Artist> {
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

### Step 2: Create the Repository Class

Next, create a repository that extends `HttpRepository`:

```java
public class ArtistRepository extends HttpRepository<Artist> {
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

**Constructor Parameters Explained:**

- `Artist.class`: Specifies the entity type for type-safe operations
- `JsonConverter`: Adds a custom JSON converter that handles UpperCamelCase field naming
- `HttpContext`: Configures the HTTP endpoint by combining base settings with the `/artists` path

### Step 3: Using the Repository

Now you can perform CRUD operations using your repository:

```java
ArtistRepository artistRepository = new ArtistRepository();

// Get all artists
List<Artist> artists = artistRepository.getAll();

// Create a new artist
Artist newArtist = Artist.builder().name("The Beatles").build();
Artist createdArtist = artistRepository.create(newArtist);

// Get artist by ID
Artist artist = artistRepository.getById("123");

// Update an artist
artist.setName("The Rolling Stones");
Artist updatedArtist = artistRepository.update(artist);

// Delete an artist
artistRepository.delete(artist);
```