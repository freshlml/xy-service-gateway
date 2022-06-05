package com.fresh.xy.gateway.predicates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.DefaultServerWebExchange;

import java.util.function.Predicate;

@Slf4j
public class FlSampleRoutePredicateFactory implements RoutePredicateFactory<FlSampleRoutePredicateFactory.Config> {

    @Override
    public void beforeApply(Config config) {
        log.info("before apply, {}", config);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return serverWebExchange -> {
            log.info("apply执行, config: {}, exchange: {}", config, serverWebExchange);

            return true;
        };
    }

    @Override
    public Config newConfig() {
        log.info("构造Config,{}", Config.class);
        return new Config("nothing");
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }


    public static class Config {
        private String nothing;
        public Config(String nothing) {
            this.nothing = nothing;
        }
        public String getNothing() {
            return nothing;
        }
        public void setNothing(String nothing) {this.nothing = nothing;}
        @Override
        public String toString() {
            return new ToStringCreator(this).append("patterns", nothing).toString();
        }
    }

    public static void main(String argv[]) {
        FlSampleRoutePredicateFactory route = new FlSampleRoutePredicateFactory();

        log.info("route name, {}",route.name()); //FlSample

        //调用apply(Consumer)获得Predicate
        Predicate<ServerWebExchange> predicate = route.apply(config -> {
            log.info("consumer Config, {}", config);
        });
        //执行Predicate
        predicate.test(new DefaultServerWebExchange(null, null, null, null, null));
        //route.applyAsync(config -> {log.info("consumer Config, {}", config);});

    }
}
