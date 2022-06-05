package com.fresh.xy.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("SampleGlobalFilter");
        ServerHttpRequest request = exchange.getRequest();

        log.info("method: {}", request.getMethod().name());
        log.info("request path: {}", request.getPath().pathWithinApplication().value());
        log.info("headers: {}", request.getHeaders());
        log.info("query params: {}", request.getQueryParams());
        Flux<DataBuffer> body = request.getBody();
        body.map(buffer -> {
            log.info("map-buffer: {}", buffer);
            return buffer;
        }).subscribe(buffer -> {
            log.info("subscribe-buffer: {}", buffer);
        }, error -> {
            log.error("some thing error: {}", error.getMessage());
        }, () -> {
            log.info("subscribe complete");
        });
        ApplicationContext context = exchange.getApplicationContext();
        Map<String, Object> attrs = exchange.getAttributes();
        log.info("attrs: {}", attrs);

        Mono<Void> result = chain.filter(exchange);

        ServerHttpResponse response = exchange.getResponse();
        log.info("response status: {}", response.getStatusCode().value());
        log.info("response headers: {}", response.getHeaders());

        return result;
    }

    @Override
    public int getOrder() {
        return -10000;
    }
}
