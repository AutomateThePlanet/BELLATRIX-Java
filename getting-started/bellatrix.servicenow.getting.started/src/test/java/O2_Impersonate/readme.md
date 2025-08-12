# Impersonate Tests

## Prerequisites

### User Setup Requirements

For the impersonate tests, at least **two users** are expected to be set on the instance:

#### User 1: Primary User
- The first user's `userName` and `password` will be read from the config file
- This user should have appropriate permissions for impersonation

#### User 2: Target User for Impersonation
- The second user's name is a test parameter
- Replace `"User Impersonate"` in the test with the actual username

## Implementation Notes

> **Important:** Ensure that the primary user has the necessary permissions to impersonate other users
