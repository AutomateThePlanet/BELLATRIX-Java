# Getting Started with Bellatrix Data Module

## Prerequisites

### 1. Add Maven Dependency

To start using the Data Module, add the following dependency to your project's `pom.xml`:

```xml
<dependency>
    <groupId>solutions.bellatrix</groupId>
    <artifactId>bellatrix.data</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. Create Configuration File

Create `testFrameworkSettings.{env}.json` inside the `src/main/resources` or `src/test/resources` folder.

## Configuration Structure

The configuration file contains several key sections:

- **dataSettings**: HTTP data source configuration for API interactions
- **httpSettings**: Nested configuration for HTTP-specific settings

### Example Configuration File

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

## Key Configuration Sections

### Data Settings (Required for HTTP APIs)

- **dataSourceType**: Set to "HTTP" for REST API interactions
- **baseUrl**: Your API's root URL
- **basePath**: Common path prefix for all endpoints
- **authentication**: Support for Bearer tokens, Basic auth, or query parameters
- **headers**: Default headers sent with every request

### Environment Variables

Use `{env_variable_name}` syntax for sensitive data:

- `{env_http_bearer_token}` - Your API bearer token
- `{env_http_username}` - Basic auth username
- `{env_http_password}` - Basic auth password

## Environment-Specific Configuration

You can create multiple configuration files for different environments:

- `testFrameworkSettings.dev.json` - Development environment
- `testFrameworkSettings.test.json` - Test environment
- `testFrameworkSettings.staging.json` - Staging environment
- `testFrameworkSettings.prod.json` - Production environment

## Environment Configuration

### 3. Create Application Properties File

Add an `application.properties` file inside the `/src/main/resources` or `/src/test/resources` folder to specify which configuration environment to use.

### Environment Mapping

The `environment` property determines which configuration file the framework will load:

| Environment Value | Configuration File Loaded | Use Case |
|------------------|---------------------------|----------|
| `dev` | `testFrameworkSettings.dev.json` | Development environment |
| `test` | `testFrameworkSettings.test.json` | Test environment |
| `staging` | `testFrameworkSettings.staging.json` | Staging environment |
| `prod` | `testFrameworkSettings.prod.json` | Production environment |

### Example Application Properties

```properties
# Set environment for configuration file selection
environment=test
```

> **Note**: Make sure your `testFrameworkSettings.{environment}.json` file exists and matches the environment value you specify.

## Using Template Configuration

For faster setup, you can use the provided template configuration file:

### Setup Steps

1. **Locate the template**: Find the example configuration file in the `template` folder of the project
2. **Copy the template**: Copy `testFrameworkSettings.template.json`
3. **Place in resources**: Paste the file into your `src/main/resources` or `src/test/resources` folder
4. **Rename appropriately**: Rename to match your environment (e.g., `testFrameworkSettings.dev.json`)
5. **Customize settings**: Update the configuration values to match your environment

> **Tip**: This template includes all necessary sections with example values, making it easier to get started quickly.

## Next Steps

After completing the configuration:

1. Create your data entities (models)
2. Set up repositories for API interactions
3. Write your first tests

*Detailed implementation examples will be provided in the following sections.*