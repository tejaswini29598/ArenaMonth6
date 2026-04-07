package com.enterprise.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Global Filter for request logging and tracing
 */
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Add request ID if not present
        if (!exchange.getRequest().getHeaders().containsKey(REQUEST_ID_HEADER)) {
            exchange.getRequest().mutate()
                    .header(REQUEST_ID_HEADER, UUID.randomUUID().toString())
                    .build();
        }

        String requestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);
        long startTime = System.currentTimeMillis();

        logger.info("Incoming Request: {} {} - RequestID: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                requestId);

        return chain.filter(exchange).doFinally(signalType -> {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Outgoing Response: {} {} - Status: {} - Duration: {}ms - RequestID: {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getPath(),
                    exchange.getResponse().getStatusCode(),
                    duration,
                    requestId);
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
