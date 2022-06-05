package com.fresh.xy.gateway.resolver;

import org.springframework.web.server.ServerWebExchange;

@FunctionalInterface
public interface ConditionalKeyResolver {
    boolean matches(ServerWebExchange exchange);
}
