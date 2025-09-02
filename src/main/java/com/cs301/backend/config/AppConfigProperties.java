package com.cs301.backend.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration properties that can be externalized via environment variables. This
 * class demonstrates how to bind environment variables to strongly-typed configuration objects.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {

  /** Application name. */
  @NotBlank private String name = "CS301 Backend Template";

  /** Application version. */
  @NotBlank private String version = "1.0.0";

  /** Application description. */
  private String description = "A standardized Spring Boot backend service template";

  /** Current environment (development, staging, production). */
  private String environment = "development";

  /** Whether debug mode is enabled. */
  private boolean debugMode = false;

  /** CORS configuration. */
  private final Cors cors = new Cors();

  /** Security configuration. */
  private final Security security = new Security();

  /** Cache configuration. */
  private final Cache cache = new Cache();

  /** Rate limiting configuration. */
  private final RateLimiting rateLimiting = new RateLimiting();

  /** Monitoring configuration. */
  private final Monitoring monitoring = new Monitoring();

  @Data
  public static class Cors {
    private List<String> allowedOrigins = List.of("http://localhost:3000");
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private List<String> allowedHeaders = List.of("*");
    private boolean allowCredentials = true;
  }

  @Data
  public static class Security {
    private final Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
      @NotBlank private String secret = "dev-secret-key-change-in-production";
      @NotNull private Long expiration = 86400L; // 24 hours in seconds
    }
  }

  @Data
  public static class Cache {
    private boolean enabled = false;
    private Long ttl = 300L; // 5 minutes in seconds
  }

  @Data
  public static class RateLimiting {
    private boolean enabled = false;
    private Integer requestsPerMinute = 100;
  }

  @Data
  public static class Monitoring {
    private boolean metricsEnabled = false;
    private boolean tracingEnabled = false;
  }
}
