package com.enterprise.gateway.config;

import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

/**
 * Rate Limiting Configuration
 */
@Configuration
public class RateLimiterConfig {
    
    /**
     * Create rate limiter bucket
     * 100 requests per minute
     */
    private final Bucket bucket = Bucket4j.builder()
            .addLimit(Bandwidth.classic(100, Refill.intervally(100, java.time.Duration.ofMinutes(1))))
            .build();

    /**
     * Configure IP resolver for rate limiting
     */
    @Bean
    public XForwardedRemoteAddressResolver addressResolver() {
        return new XForwardedRemoteAddressResolver(1);
    }

    public Bucket resolveBucket() {
        return bucket;
    }
}
