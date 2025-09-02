package com.cs301.backend.controller;

import com.cs301.backend.config.AppConfigProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Configuration controller that demonstrates how to use externalized configuration. This controller
 * shows current configuration values (non-sensitive only).
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
@Tag(name = "Configuration", description = "Configuration management endpoints")
public class ConfigController {

  private final AppConfigProperties appConfig;
  private final Environment environment;

  /**
   * Get application information including environment-specific configuration.
   *
   * @return Application configuration information
   */
  @GetMapping("/info")
  @Operation(
      summary = "Get application information",
      description = "Returns application configuration information and active profiles")
  public ResponseEntity<Map<String, Object>> getApplicationInfo() {
    Map<String, Object> info = new HashMap<>();

    // Basic application information
    info.put("name", appConfig.getName());
    info.put("version", appConfig.getVersion());
    info.put("description", appConfig.getDescription());
    info.put("environment", appConfig.getEnvironment());
    info.put("debugMode", appConfig.isDebugMode());

    // Active profiles
    info.put("activeProfiles", environment.getActiveProfiles());
    info.put("defaultProfiles", environment.getDefaultProfiles());

    // CORS configuration (non-sensitive)
    Map<String, Object> corsInfo = new HashMap<>();
    corsInfo.put("allowedMethods", appConfig.getCors().getAllowedMethods());
    corsInfo.put("allowedHeaders", appConfig.getCors().getAllowedHeaders());
    corsInfo.put("allowCredentials", appConfig.getCors().isAllowCredentials());
    // Note: We don't expose allowed origins for security reasons
    info.put("cors", corsInfo);

    // Cache configuration
    Map<String, Object> cacheInfo = new HashMap<>();
    cacheInfo.put("enabled", appConfig.getCache().isEnabled());
    cacheInfo.put("ttl", appConfig.getCache().getTtl());
    info.put("cache", cacheInfo);

    // Rate limiting configuration
    Map<String, Object> rateLimitInfo = new HashMap<>();
    rateLimitInfo.put("enabled", appConfig.getRateLimiting().isEnabled());
    rateLimitInfo.put("requestsPerMinute", appConfig.getRateLimiting().getRequestsPerMinute());
    info.put("rateLimiting", rateLimitInfo);

    // Monitoring configuration
    Map<String, Object> monitoringInfo = new HashMap<>();
    monitoringInfo.put("metricsEnabled", appConfig.getMonitoring().isMetricsEnabled());
    monitoringInfo.put("tracingEnabled", appConfig.getMonitoring().isTracingEnabled());
    info.put("monitoring", monitoringInfo);

    return ResponseEntity.ok(info);
  }

  /**
   * Get current active profile information.
   *
   * @return Current profile information
   */
  @GetMapping("/profile")
  @Operation(
      summary = "Get active profile",
      description = "Returns information about the currently active Spring profile")
  public ResponseEntity<Map<String, Object>> getProfileInfo() {
    Map<String, Object> profileInfo = new HashMap<>();

    profileInfo.put("activeProfiles", environment.getActiveProfiles());
    profileInfo.put("defaultProfiles", environment.getDefaultProfiles());
    profileInfo.put("environment", appConfig.getEnvironment());

    // Add profile-specific information
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles.length > 0) {
      String primaryProfile = activeProfiles[0];
      profileInfo.put("primaryProfile", primaryProfile);

      // Add profile-specific recommendations
      switch (primaryProfile) {
        case "dev":
          profileInfo.put("description", "Development environment with debugging enabled");
          profileInfo.put(
              "features", new String[] {"H2 Console", "Debug Logging", "All Actuator Endpoints"});
          break;
        case "staging":
          profileInfo.put("description", "Staging environment for testing");
          profileInfo.put(
              "features",
              new String[] {"PostgreSQL", "Moderate Logging", "Limited Actuator Endpoints"});
          break;
        case "prod":
          profileInfo.put("description", "Production environment with optimizations");
          profileInfo.put(
              "features", new String[] {"PostgreSQL", "Minimal Logging", "Security Hardened"});
          break;
        default:
          profileInfo.put("description", "Custom environment configuration");
          break;
      }
    }

    return ResponseEntity.ok(profileInfo);
  }

  /**
   * Health check endpoint that can be used to verify configuration.
   *
   * @return Health status with configuration verification
   */
  @GetMapping("/health")
  @Operation(
      summary = "Configuration health check",
      description = "Verifies that essential configuration is properly set")
  public ResponseEntity<Map<String, Object>> getConfigHealth() {
    Map<String, Object> health = new HashMap<>();
    boolean isHealthy = true;

    // Check if essential configuration is present
    if (appConfig.getName() == null || appConfig.getName().trim().isEmpty()) {
      health.put("name", "MISSING");
      isHealthy = false;
    } else {
      health.put("name", "OK");
    }

    if (appConfig.getVersion() == null || appConfig.getVersion().trim().isEmpty()) {
      health.put("version", "MISSING");
      isHealthy = false;
    } else {
      health.put("version", "OK");
    }

    // Check if JWT secret is properly configured for non-dev environments
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles.length > 0 && !activeProfiles[0].equals("dev")) {
      String jwtSecret = appConfig.getSecurity().getJwt().getSecret();
      if (jwtSecret.equals("dev-secret-key-change-in-production")) {
        health.put("jwtSecret", "INSECURE - Using default development secret");
        isHealthy = false;
      } else {
        health.put("jwtSecret", "OK");
      }
    } else {
      health.put("jwtSecret", "OK - Development environment");
    }

    health.put("status", isHealthy ? "HEALTHY" : "UNHEALTHY");

    return ResponseEntity.ok(health);
  }
}
