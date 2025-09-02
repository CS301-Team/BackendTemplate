# Configuration Management Documentation

This document explains how to use the externalized configuration system with Spring Profiles in the CS301 Backend Template.

## Overview

The application uses Spring Boot's configuration management features to externalize all configurations using environment variables. This allows for easy environment swapping and secure configuration management.

## Configuration Files

### Main Configuration Files

- `application.yml` - Base configuration with environment variable placeholders
- `application-dev.yml` - Development environment specific configuration
- `application-staging.yml` - Staging environment specific configuration
- `application-prod.yml` - Production environment specific configuration

### Environment Variables File

- `.env.example` - Example environment variables file showing all available configuration options

## Spring Profiles

The application supports three main profiles:

### Development Profile (`dev`)

- **Purpose**: Local development
- **Database**: H2 in-memory database
- **Logging**: DEBUG level with verbose output
- **Security**: Relaxed for development
- **Actuator**: All endpoints exposed
- **Swagger**: Enabled with all features

### Staging Profile (`staging`)

- **Purpose**: Pre-production testing
- **Database**: PostgreSQL
- **Logging**: INFO level
- **Security**: Production-like with some debugging
- **Actuator**: Limited endpoints exposed
- **Swagger**: Enabled

### Production Profile (`prod`)

- **Purpose**: Live production environment
- **Database**: PostgreSQL with connection pooling
- **Logging**: Minimal INFO/WARN level
- **Security**: Fully hardened
- **Actuator**: Minimal endpoints exposed
- **Swagger**: Disabled for security

## Setting Up Environment Variables

### 1. Copy the Example File

```bash
cp .env.example .env.local
```

### 2. Edit Environment Variables

Edit `.env.local` with your specific configuration values:

```bash
# Set the active profile
SPRING_PROFILES_ACTIVE=dev

# Application configuration
APP_NAME=My Backend Application
SERVER_PORT=8081

# Database configuration (when JPA is enabled)
DEV_DB_URL=jdbc:h2:mem:mydevdb
DEV_DB_USERNAME=sa
DEV_DB_PASSWORD=

# Security configuration
DEV_JWT_SECRET=my-development-secret-key
```

### 3. Load Environment Variables

#### Option A: Using an .env file with your IDE

Most IDEs support loading environment variables from `.env` files.

#### Option B: Export variables in your shell

```bash
export SPRING_PROFILES_ACTIVE=dev
export SERVER_PORT=8081
# ... other variables
```

#### Option C: Set variables in your deployment environment

For production deployments, set environment variables in your deployment platform (Docker, Kubernetes, cloud services, etc.).

## Configuration Classes

### AppConfigProperties

Located at `com.cs301.backend.config.AppConfigProperties`, this class demonstrates how to bind environment variables to strongly-typed configuration objects.

Example usage:

```java
@Autowired
private AppConfigProperties appConfig;

public void someMethod() {
    String appName = appConfig.getName(); // Gets value from APP_NAME env var
    boolean cacheEnabled = appConfig.getCache().isEnabled(); // Gets from CACHE_ENABLED
}
```

### CorsConfig

Shows how to use configuration properties for CORS setup using environment variables.

## Running with Different Profiles

### Command Line

```bash
# Development
java -jar target/backend-template.jar --spring.profiles.active=dev

# Staging
java -jar target/backend-template.jar --spring.profiles.active=staging

# Production
java -jar target/backend-template.jar --spring.profiles.active=prod
```

### Environment Variable

```bash
export SPRING_PROFILES_ACTIVE=prod
java -jar target/backend-template.jar
```

### IDE Configuration

Set the environment variable `SPRING_PROFILES_ACTIVE=dev` in your IDE's run configuration.

## Configuration Endpoints

The application provides REST endpoints to inspect configuration:

### `/api/config/info`

Returns general application configuration information.

### `/api/config/profile`

Returns information about the currently active profile.

### `/api/config/health`

Performs health checks on configuration, including security validation.

## Environment Variable Reference

### Application Settings

- `APP_NAME` - Application name
- `APP_VERSION` - Application version
- `APP_DESCRIPTION` - Application description
- `SPRING_PROFILES_ACTIVE` - Active Spring profile

### Server Settings

- `SERVER_PORT` - Server port (default: 8080)
- `SERVER_CONTEXT_PATH` - Context path (default: /api)

### Database Settings (when JPA is enabled)

- `*_DB_URL` - Database URL
- `*_DB_USERNAME` - Database username
- `*_DB_PASSWORD` - Database password
- `*_DB_DRIVER` - Database driver class

### Security Settings

- `*_JWT_SECRET` - JWT signing secret
- `*_JWT_EXPIRATION` - JWT expiration time in seconds

### CORS Settings

- `*_CORS_ALLOWED_ORIGINS` - Allowed origins (comma-separated)
- `*_CORS_ALLOWED_METHODS` - Allowed HTTP methods
- `*_CORS_ALLOWED_HEADERS` - Allowed headers

### Logging Settings

- `*_LOG_LEVEL_*` - Log levels for different packages
- `*_LOG_FILE` - Log file path

### Monitoring Settings

- `*_MANAGEMENT_ENDPOINTS` - Exposed actuator endpoints
- `*_SWAGGER_ENABLED` - Enable/disable Swagger UI

_Note: Replace `_` with profile prefix (DEV*, STAGING*, PROD\_) or leave empty for base configuration.\*

## Security Considerations

1. **Never commit sensitive environment variables** to version control
2. **Use strong, unique secrets** for production environments
3. **Rotate secrets regularly** in production
4. **Limit actuator endpoint exposure** in production
5. **Disable Swagger** in production environments
6. **Use secure database credentials** and connection parameters

## Best Practices

1. **Use profile-specific prefixes** for environment variables (DEV*, STAGING*, PROD\_)
2. **Provide sensible defaults** in configuration files
3. **Document all configuration options** in the `.env.example` file
4. **Validate configuration** at startup using the health check endpoint
5. **Use the configuration classes** instead of `@Value` annotations for complex configurations
6. **Keep sensitive information** in environment variables, not in config files

## Troubleshooting

### Profile Not Loading

- Check that `SPRING_PROFILES_ACTIVE` is set correctly
- Verify the profile-specific YAML file exists
- Check application logs for profile activation messages

### Configuration Not Applied

- Verify environment variables are set and exported
- Check for typos in environment variable names
- Use the `/api/config/info` endpoint to see current configuration

### Database Connection Issues

- Verify database environment variables are correct
- Check that the database driver is included in dependencies
- Ensure the database is running and accessible

### Missing Configuration

- Use the `/api/config/health` endpoint to check for missing required configuration
- Check the application logs for configuration binding errors
