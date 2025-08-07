# HTTP Settings

## Overview

This guide explains how to configure HTTP data sources in the Bellatrix framework. The configuration is typically placed in your `testFrameworksSettings.json` file, located in `src/main/resources`.

## HttpSettings

The `HttpSettings` class configures HTTP data source settings for your application. Add this configuration to your `testFrameworksSettings.json` file to define base URLs, headers, authentication methods, and other HTTP-specific settings that apply to all repository requests.

### Configuration Example

```json
{  
  "dataSettings": {
    "dataSourceType": "HTTP",
    "httpSettings": {
      "baseUrl": "http://localhost:3001/",
      "basePath": "/api",
      "urlEncoderEnabled": "false",
      "contentType": "application/json",
      "headers": [
        {
          "name": "Accept",
          "value": "application/json"
        }
      ],
      "authentication": {
        "method": "Bearer",
        "options": [
          {
            "type": "Bearer",
            "token": "{env_http_bearer_token}"
          },
          {
            "type": "Basic",
            "username": "{env_http_username}",
            "password": "{env_http_password}"
          },
          {
            "insertionOrder": "start",
            "type": "QueryParameters",
            "key": "{env_http_query_key}",
            "token": "{env_http_query_token}"
          }
        ]
      }
    }
  }
}
```

> **Environment Variables**: Use `{env_variable_name}` syntax to reference environment variables for sensitive data like tokens and passwords.

### Configuration Properties

| Property | Description | Example |
|----------|-------------|---------|
| `dataSourceType` | Type of data source (currently supports "HTTP") | `"HTTP"` |
| `baseUrl` | Root URL of your API endpoint | `"http://localhost:3001/"` |
| `basePath` | Path appended to the base URL for all API requests | `"/api"` |
| `urlEncoderEnabled` | Whether to enable URL encoding for requests | `"false"` |
| `contentType` | Content type for requests | `"application/json"` |
| `authentication` | Authentication configuration with multiple method support | See authentication section below |
| `headers` | Default headers included in every request | Custom headers array |

### Authentication Methods

The framework supports multiple authentication methods that can be configured simultaneously:

- **Bearer Token**: Uses authorization header with Bearer token
- **Basic Authentication**: Uses username/password with Basic auth header
- **Query Parameters**: Adds authentication parameters to the URL query string

**Selecting Default Method:**

To specify a default authentication method, set the **method** property in the **authentication** section to one of the supported types: "Bearer", "Basic", or "QueryParameters". The framework will apply this method to all requests in your project.

**Example:**
To use Basic authentication, simply change the method property to "Basic":
```json
  "authentication": {
    "method": "Basic",
    "options": [
      {
        "type": "Basic",
        "username": "{env_http_username}",
        "password": "{env_http_password}"
      }
    ]
  }
```
>[!CAUTION]
>Ensure you have set the environment variables `http_username` and `http_password` on your machine. While you can hardcode these values directly in the configuration, using environment variables is the recommended approach for security reasons.

> [!NOTE]
> The current version supports these three authentication methods. Future releases will include additional authentication types and the ability to create custom, project-specific authentication methods.