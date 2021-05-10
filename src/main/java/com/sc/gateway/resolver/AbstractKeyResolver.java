package com.sc.gateway.resolver;

import com.sc.gateway.constant.GatewayConstants;
import com.sc.gateway.enums.CustomKeyResolverEnum;
import com.sc.gateway.utils.GatewayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class AbstractKeyResolver implements KeyResolver, ConditionalKeyResolver {

    @Override
    public boolean matches(ServerWebExchange exchange) {
        String value = GatewayUtils.getPropertyFormContext(exchange.getApplicationContext(), GatewayConstants.RATE_LIMITER_KEY_RESOLVER);
        CustomKeyResolverEnum valueEnum = CustomKeyResolverEnum.getByValue(value);
        if(valueEnum != null) {
            return matchesInternal(valueEnum);
        } else {
            log.warn("请检查{}的配置值，如果没有配置或者配置错误将使用默认的KeyResolver", GatewayConstants.RATE_LIMITER_KEY_RESOLVER);
        }
        return false;
    }

    protected boolean matchesInternal(CustomKeyResolverEnum keyResolverEnum) {
        return false;
    }

    public static class NoQualifierKeyResolver extends AbstractKeyResolver {

        @Override
        public Mono<String> resolve(ServerWebExchange exchange) {
            return Mono.empty();
        }

        @Override
        public boolean matches(ServerWebExchange exchange) {
            return super.matches(exchange);
        }

        @Override
        protected boolean matchesInternal(CustomKeyResolverEnum keyResolverEnum) {
            if(keyResolverEnum != null && keyResolverEnum==CustomKeyResolverEnum.NO_QUALIFIER) return true;
            return false;
        }

    }

}
