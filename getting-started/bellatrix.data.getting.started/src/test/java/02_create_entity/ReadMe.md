# HttpEntity<TIdentifier, TEntity>

`HttpEntity<TIdentifier, TEntity>` is the base class for all data entity classes in your application.

**Key Features:**

- Validates whether the entity identifier is properly set, ensuring entities are ready for operations like getById, update, or delete
- Maintains a reference to the HTTP Response object, enabling testing of request-specific properties such as status code, headers, and response body

All your data entities should extend this class to inherit these capabilities.

**Example Usage:**

Let's create an entity class that represents an artist in our music shop application:

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

**Key Points:**

- The class extends `HttpEntity<String, Artist>` where `String` is the identifier type
- `@SerializedName` annotations map JSON fields to Java properties for proper serialization
- The `getIdentifier()` method returns the unique identifier used for CRUD operations


# Entity<TIdentifier, TEntity> Class

`HttpEntity` extends the `Entity<TIdentifier, TEntity>` class, which provides the core interface for all data entities regardless of their data source.

**Key Features:**

- **Direct Entity Operations**: Perform database/API operations directly on entity instances without requiring separate repository calls
- **Consistent Behavior**: Maintain uniform operation patterns across all entity types in your application
- **Flexible Identifier Types**: Support different identifier types (UUID, String, Integer, etc.) based on your data model requirements
- **Reduced Boilerplate**: Minimize code duplication by embedding CRUD operations directly within entity classes

This architecture streamlines data management by reducing the lines of code needed to create and manage entities. You can focus on defining your data model while the framework handles the underlying HTTP interactions and operations.

## Usage Patterns

There are three main approaches to working with entities and repositories:

### Option 1: Explicit Repository Usage (Verbose but Clear)

Create a repository instance and use it explicitly for operations:

```java
ArtistRepository artistRepository = new ArtistRepository();
Artist artist = Artist.builder().name("Arthur Conan Doyle").build();
Artist createdArtist = artistRepository.create(artist);
```

**Benefits:** Clear separation of concerns, explicit repository management

#### Option 2: Inline Repository Usage (Concise)

Combine repository creation and operation in a single statement:

```java
Artist artist = new ArtistRepository().create(Artist.builder().name("J.K.Rowling").build());
```

**Benefits:** Fewer lines of code, good for simple operations

#### Option 3: Bellatrix Entity-Centric Approach

Register repositories once and perform operations directly on entities:

```java
RepositoryFactory.INSTANCE.registerRepository(Artist.class, ArtistRepository.class);

Artist artist = Artist.builder().name("James Clavell").build().create();
```

**Benefits:** Most concise, entity-focused design, automatic repository management