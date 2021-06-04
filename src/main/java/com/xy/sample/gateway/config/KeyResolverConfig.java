package com.xy.sample.gateway.config;

import com.xy.sample.gateway.resolver.DefaultMixedKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyResolverConfig {

    @Bean
    public DefaultMixedKeyResolver mixedKeyResolver() {
        DefaultMixedKeyResolver mixedKeyResolver = new DefaultMixedKeyResolver();
        return mixedKeyResolver;
    }

}
