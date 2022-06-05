package com.fresh.xy.gateway.resolver;

import com.fresh.xy.gateway.constant.GatewayConstants;
import com.fresh.xy.gateway.enums.CustomKeyResolverEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class RequestUserKeyResolver extends AbstractKeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        //取request header的作为唯一标记
        String user_sign = exchange.getRequest().getHeaders().get(GatewayConstants.REQUEST_HEADER_USER_UNIQUE_SIGN).get(0); //防止null pointer,先调用matches
        return Mono.just(user_sign);
    }

    @Override
    public boolean matches(ServerWebExchange exchange) {
        boolean preMatches = super.matches(exchange);
        List<String> user_sign = exchange.getRequest().getHeaders().get(GatewayConstants.REQUEST_HEADER_USER_UNIQUE_SIGN);
        boolean matchers = user_sign != null && user_sign.size() > 0;
        if(preMatches && !matchers) {  //todo 如果Mono.isEmpty返回会如何？
            log.warn("matchers failed: 此类即将被用做KeyResolver，但是由于没有必要的header[{}]设置导致match失败,将使用默认的KeyResolver", GatewayConstants.REQUEST_HEADER_USER_UNIQUE_SIGN);
        }
        return preMatches && matchers;
    }

    @Override
    protected boolean matchesInternal(CustomKeyResolverEnum keyResolverEnum) {
        if(keyResolverEnum != null && keyResolverEnum==CustomKeyResolverEnum.USER_SIGN) return true;
        return false;
    }

}
