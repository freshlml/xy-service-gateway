package com.xy.sample.gateway.resolver;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class MixedKeyResolver extends AbstractKeyResolver {

    private final static AbstractKeyResolver DEFAULT_KEY_RESOLVER = new PrincipalNameKeyResolverAdapter();

    private List<AbstractKeyResolver> keyResolverDelegates = new ArrayList<>();

    public MixedKeyResolver() {
        addCustomKeyResolvers();
    }

    public void addToKeyResolvers(AbstractKeyResolver keyResolver) {
        keyResolverDelegates.add(keyResolver);
    }
    protected abstract void addCustomKeyResolvers();

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        //find the first matches or default (List前面的元素被优先检测)
        Optional<AbstractKeyResolver> result = keyResolverDelegates.stream().filter(keyResolver -> keyResolver.matches(exchange)).findFirst();
        return result.orElse(DEFAULT_KEY_RESOLVER).resolve(exchange);
    }


}
