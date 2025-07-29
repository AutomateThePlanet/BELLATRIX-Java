# Getting Started with Bellatrix Data Module

## Overview

The Data Module simplifies data management in your tests by providing a clean, type-safe interface for API interactions.

## Who Is This Guide For?

Test automation engineers looking to streamline data management in their automation projects.

## Why Use the Data Module?

**Simplicity** - Eliminate boilerplate HTTP request code and focus on test logic

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

**Speed**
Built-in infrastructure enables you to start writing tests immediately—no need to learn complex HTTP client libraries.

Traditional Approach:
Requires creating an HTTP client, building a request, and then executing it with the client:
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
.uri(URI.create("http://localhost:3001/api/artists"))
.header("Content-Type", "application/json")
.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(artist)))
.build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
```
With Bellatrix:
Just define your HTTP entity and create a repository instance—no boilerplate required.

HttpEntity
Represents the data objects your API works with. Each entity corresponds to a resource (such as Artist, Album, etc.) and defines the structure and fields of that resource in your application.

HttpRepository
Acts as the client for performing CRUD (Create, Read, Update, Delete) operations on your entities. It manages sending requests to the API and handling responses, providing a simple interface for interacting with your data source.

```java
 ArtistRepository artistRepository = new ArtistRepository();
 List<Artist> artists = artistRepository.getAll();
```

No more manual client setup or request building—just focus on your test logic.



**Type Safety**
Work with strongly-typed models, with all conversions handled automatically. No more casting in tests or dealing with raw JSON responses.

Traditional Approach:
Typically requires boilerplate code to convert response data into objects, often using a fixed library that may not be easily replaced:

```java
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
Artist createdArtist = gson.fromJson(response.body(), Artist.class);
```
With Bellatrix:

Bellatrix provides built-in logic for converting between strings and objects, so you don’t need to depend on a specific library or write casting code. Type conversions happen behind the scenes, letting you focus on your test logic:

```java
 List<Artist> artists = artistRepository.getAll();
```
Once the request is sent, the response is automatically handled and the data is returned in the correct type—no explicit configuration needed in your tests.

**Flexibility**
 Begin with simple scenarios and extend later as needed.
The Data Module’s greatest strength is its extensibility. You can easily customize and enhance your client (HttpRepository) and requests specific properties, or custom converters—whenever your project requirements evolve.

You can extend:

HttpRepository: Add custom methods or override existing CRUD operations to fit your business logic.
HttpContext: Dynamically modify request context, such as adding path parameters, headers, or query parameters.
Object Mapper (e.g., JsonConverter): Plug in your own serialization/deserialization logic for custom data formats.


## Key Features

- Abstract away HTTP request complexity
- Built-in serialization and response parsing
- Repository pattern for data access
- Minimal setup required

Transform complex API interactions into intuitive operations that focus on business logic, not infrastructure code.