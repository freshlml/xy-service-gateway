package com.sc.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.PrincipalNameKeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class PrincipalNameKeyResolverAdapter extends AbstractKeyResolver {
    private static final KeyResolver DEFAULT_KEY_RESOLVER_DELEGATE = new PrincipalNameKeyResolver();

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return DEFAULT_KEY_RESOLVER_DELEGATE.resolve(exchange);
    }

    @Override
    public boolean matches(ServerWebExchange exchange) {
        return true; //always return true,通常被用于List的最后一个元素作为default的KeyResolver
    }

}
