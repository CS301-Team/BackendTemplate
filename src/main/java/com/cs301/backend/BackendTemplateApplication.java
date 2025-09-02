package com.cs301.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info =
                @Info(
                        title = "CS301 Backend API",
                        version = "1.0",
                        description = "API documentation for CS301 Backend Template"))
public final class BackendTemplateApplication {

    /** Private constructor to prevent instantiation of this utility class. */
    private BackendTemplateApplication() {}

    public static void main(final String[] args) {
        SpringApplication.run(BackendTemplateApplication.class, args);
    }
}
