package com.xy.gateway.resolver;

import com.xy.gateway.enums.CustomKeyResolverEnum;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AddrKeyResolver extends AbstractKeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(getRemoteAddress(exchange));
    }

    @Override
    public boolean matches(ServerWebExchange exchange) {
        return super.matches(exchange);
    }

    @Override
    protected boolean matchesInternal(CustomKeyResolverEnum keyResolverEnum) {
        if(keyResolverEnum != null && keyResolverEnum==CustomKeyResolverEnum.ADDR) return true;
        return false;
    }

    private static String getRemoteAddress(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        if (ip != null) {
            return ip.split(",")[0];
        } else {
            ip = request.getHeaders().getFirst("X-Real-IP");
            return ip != null ? ip.split(",")[0] : request.getRemoteAddress().toString();
        }
    }
}
