# Configuring HTTP Context

## Overview

This guide explains how to  use HttpContext for dynamic configuration, and customize requests for your specific API endpoints.

## HttpContext Class

The `HttpContext` class acts as a bridge between your base configuration and actual HTTP requests. It encapsulates HTTP operation settings and provides dynamic request configuration capabilities.

### Key Capabilities

- **URL Construction**: Combines base settings with dynamic path parameters
- **Request Customization**: Allows per-repository endpoint customization
- **Parameter Management**: Supports both path parameters and query parameters
- **Configuration Extension**: Builds upon base settings from `testFrameworksSettings.json`

## Dynamic Configuration and URL Construction

The `HttpContext` takes your base configuration from `testFrameworksSettings.json` and allows repository-specific customization.

### URL Building Process

The framework constructs URLs by combining multiple components:

| Step | Component | Source | Example |
|------|-----------|--------|---------|
| 1 | Base URL | Configuration file | `http://localhost:3001` |
| 2 | Base Path | Configuration file | `/api` |
| 3 | Path Parameters | Dynamic via `addPathParameter()` | `/artists` |
| 4 | **Final URL** | **Combined result** | **`http://localhost:3001/api/artists`** |

### Repository Implementation Example

```java
public class ArtistRepository extends HttpRepository<String, Artist> {
    public ArtistRepository() {
        super(Artist.class, new JsonConverter(), () -> {
            // Get base HTTP settings from configuration
            var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
            
            // Create HttpContext with base settings
            var httpContext = new HttpContext(httpSettings);
            
            // Add repository-specific path parameter
            httpContext.addPathParameter("/artists");
            
            // The resulting URL will be: {baseUrl}{basePath}/artists
            // Example: http://localhost:3001/api/artists
            return httpContext;
        });
    }
}
```

### Additional HttpContext Methods

```java
// Add multiple path parameters
httpContext.addPathParameter("/artists");
httpContext.addPathParameter("/123"); // Results in: /api/artists/123

// Add query parameters
httpContext.addQueryParameter("limit", "10");
httpContext.addQueryParameter("offset", "0"); // Results in: /api/artists?limit=10&offset=0
```