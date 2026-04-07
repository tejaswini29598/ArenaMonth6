package com.enterprise.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Configuration;

/**
 * API Gateway Application - Main entry point for request routing
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    /**
     * Configure API Gateway routes
     */
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://auth-service"))

                // User Service
                .route("user-service", r -> r
                        .path("/api/v1/users/**")
                        .filters(f -> f
                                .stripPrefix(2)
                                .addRequestHeader("X-Request-ID", "request-id-value"))
                        .uri("lb://user-service"))

                // Product Service
                .route("product-service", r -> r
                        .path("/api/v1/products/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://product-service"))

                // Order Service
                .route("order-service", r -> r
                        .path("/api/v1/orders/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://order-service"))

                // Notification Service
                .route("notification-service", r -> r
                        .path("/api/v1/notifications/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://notification-service"))

                .build();
    }

    /**
     * Configure OpenAPI documentation
     */
    @Bean
    public OpenAPI enterpriseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Platform API Gateway")
                        .version("2.0.0")
                        .description("Production-ready API Gateway with request routing, authentication, and rate limiting")
                        .contact(new Contact()
                                .name("Enterprise Platform Team")
                                .email("platform@enterprise.com")
                                .url("https://enterprise-platform.com")));
    }
}
