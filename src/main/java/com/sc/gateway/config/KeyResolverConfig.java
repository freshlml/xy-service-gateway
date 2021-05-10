package com.sc.gateway.config;

import com.sc.gateway.resolver.DefaultMixedKeyResolver;
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
