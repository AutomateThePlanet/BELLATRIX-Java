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

**With Bellatrix:Traditional Approach:**
Just define your HTTP entity and create a repository instance - no-boilerplate required.


HttpEntity
Represents the data objects your API works with. Each entity corresponds to a resource (such as Artist, Album, etc.) and defines the structure and fields of that resource in your application.

HttpRepository
Acts as the client for performing CRUD (Create, Read, Update, Delete) operations on your entities. It manages sending requests to the API and handling responses, providing a simple interface for interacting with your data source.

```java
 ArtistRepository artistRepository = new ArtistRepository();
 List<Artist> artists = artistRepository.getAll();
```
