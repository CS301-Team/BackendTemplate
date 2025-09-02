package com.cs301.backend.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Tests for configuration properties and environment variable binding. */
@SpringBootTest
@ActiveProfiles("dev")
class AppConfigPropertiesTest {

  @Autowired private AppConfigProperties appConfig;

  @Test
  void shouldLoadDefaultConfiguration() {
    // Test that default values are loaded correctly
    assertThat(appConfig.getName()).isEqualTo("CS301 Backend Template");
    assertThat(appConfig.getVersion()).isEqualTo("1.0.0");
    assertThat(appConfig.getEnvironment()).isEqualTo("development");
    assertThat(appConfig.isDebugMode()).isTrue(); // dev profile should enable debug mode
  }

  @Test
  void shouldLoadCorsConfiguration() {
    // Test CORS configuration
    assertThat(appConfig.getCors().getAllowedOrigins()).isNotEmpty();
    assertThat(appConfig.getCors().getAllowedMethods()).contains("GET", "POST", "PUT", "DELETE");
    assertThat(appConfig.getCors().isAllowCredentials()).isTrue();
  }

  @Test
  void shouldLoadSecurityConfiguration() {
    // Test security configuration
    assertThat(appConfig.getSecurity().getJwt().getSecret()).isNotBlank();
    assertThat(appConfig.getSecurity().getJwt().getExpiration()).isPositive();
  }

  @Test
  void shouldLoadCacheConfiguration() {
    // Test cache configuration (disabled in dev by default)
    assertThat(appConfig.getCache().isEnabled()).isFalse();
    assertThat(appConfig.getCache().getTtl()).isPositive();
  }
}
