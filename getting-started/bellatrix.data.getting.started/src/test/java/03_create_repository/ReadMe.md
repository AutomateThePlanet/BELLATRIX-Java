# HTTP Repositories

**Getting Started**
The `HttpRepository` is the base class for all repositories that interact with your data source via Http calls. It provides methods to perform CRUD operations on the data.

**Key Features:**

- `<THttpEntity extends HttpEntity>` this allow you to define the type of entity that the repository will handle, ensuring type safety and consistency across your data operations
- Provides a consistent interface for data operations across different repositories
- Handles HTTP-specific details like request context and response handling

- Provides a consistent interface for data operations across different repositories
- Handles HTTP-specific details like request context and response handling

**How It Works:**

The `HttpRepository` uses three core components to manage data operations:

- **repositoryContext** (`HttpContext`): The base context for the repository, initialized in the constructor with settings specific to the data source, such as base path and resource-specific settings
- **requestContext** (`HttpContext`): The context for the current request, which can include headers, query parameters, and other request-specific settings
- **objectConverter** (`ObjectConverter`): An interface that defines methods for converting between string and Java objects. There is an out-of-the-box implementation for converting between JSON and Java objects, but you can implement your own converter if needed (for example, for XML or other formats)

**Request Flow:**

1. When the repository is created, `repositoryContext` saves the base context;
2. For each request, `requestContext` is used to handle request-specific parameters like headers and query parameters
3. When a response with data is received, `objectConverter` converts the response data into Java objects if applicable
4. At the end of the request, the request context is reset to the repository context

This design enables reusing the repository context as a foundation for all requests while allowing request-specific customization through the request context. This approach allows maintaining a single repository instance for all operations, with no overlap between different requests, ensuring that each request can be tailored to its specific needs without affecting others.

**Example Usage:**

To demonstrate how to use the `HttpRepository`, let's create a complete example with an `Artist` entity and its corresponding repository.

#### Step 1: Create the Entity Class
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
#### Step 2: Create the Repository Class

Next, create a repository that extends `HttpRepository`:

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

**Constructor Parameters Explained:**

- `Artist.class`: Specifies the entity type for type-safe operations
- `JsonConverter`: adding a custom JSON converter that handles UpperCamelCase field naming
- `HttpContext`: Configures the HTTP endpoint by combining base settings with the `/artists` path

#### Step 3: Using the Repository

Now you can perform CRUD operations using your repository:

```java
ArtistRepository artistRepository = new ArtistRepository();

ArtistRepository artists = artistRepository.getAll();
```