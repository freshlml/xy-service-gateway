package com.xy.sample.gateway.resolver;

import org.springframework.web.server.ServerWebExchange;

@FunctionalInterface
public interface ConditionalKeyResolver {
    boolean matches(ServerWebExchange exchange);
}
