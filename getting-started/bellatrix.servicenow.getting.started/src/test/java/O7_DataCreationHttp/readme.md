# Prerequisites for Using Bellatrix Data API with Service Now Applications

This guide outlines the necessary steps to integrate Bellatrix Data API features with Service Now applications.

## 1. Create Entity Class

Create an Entity class that extends `ServiceNowEntity`.

**Example:**
```java
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TableTarget("incident")
public class Incident extends ServiceNowEntity<Incident> {
    @SerializedName("caller_id")
    String caller;
    
    @SerializedName("short_description")
    String shortDescription;
}
```

## 2. Create Repository Class
Create a Repository class that extends TableApiRepository.

Parameters:
EntityName class
ServiceNow Table: table endpoint for the Entity members

**Example:**
```java
public class IncidentRepository extends TableApiRepository<Incident> {
    public IncidentRepository() {
        super(Incident.class, "incident");
    }
}
```
Note: Another option is to use the table name from ProjectTable enum.

## 3. Configure Data Settings
   Set the actual parameters in the dataSettings section of your testFrameworkConfig file.

```json
{
  "dataSettings": {
    "dataSourceType": "HTTP",
    "httpSettings": {
      "baseUrl": "baseUrl",
      "basePath": "/api/now/table",
      "urlEncoderEnabled": "false",
      "contentType": "application/json",
      "headers": [
        {
          "name": "Accept",
          "value": "application/json"
        }
      ],
      "authentication": {
        "method": "Basic",
        "options": [
          {
            "type": "Bearer",
            "token": "token"
          },
          {
            "type": "Basic",
            "username": "user",
            "password": "pass"
          },
          {
            "insertionOrder": "start",
            "type": "QueryParameters",
            "key": "user",
            "token": "token"
          }
        ]
      }
    }
  }
}

```
**Data Settings Configuration Details**:
Required Settings for HTTP APIs
    dataSourceType: Set to "HTTP" for REST API interactions
    baseUrl: Your API's root URL
    basePath: Common path prefix for all endpoints
    authentication: Support for Bearer tokens, Basic auth, or query parameters
        Set Method to Basic
        Set credentials for the selected type method

headers: Default headers sent with every request

**Authentication Example:**

```json
{
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
}

```