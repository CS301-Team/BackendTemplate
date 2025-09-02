package com.cs301.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * CORS configuration using externalized properties. This demonstrates how to use environment-driven
 * configuration for CORS settings.
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

  private final AppConfigProperties appConfig;

  /**
   * Configure CORS settings using externalized configuration properties.
   *
   * @return CorsConfigurationSource configured with environment variables
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Set allowed origins from environment variables
    configuration.setAllowedOriginPatterns(appConfig.getCors().getAllowedOrigins());

    // Set allowed methods from environment variables
    configuration.setAllowedMethods(appConfig.getCors().getAllowedMethods());

    // Set allowed headers from environment variables
    configuration.setAllowedHeaders(appConfig.getCors().getAllowedHeaders());

    // Set allow credentials from environment variables
    configuration.setAllowCredentials(appConfig.getCors().isAllowCredentials());

    // Apply configuration to all paths
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
