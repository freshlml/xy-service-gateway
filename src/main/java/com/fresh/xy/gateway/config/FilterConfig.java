package com.fresh.xy.gateway.config;

import com.fresh.xy.gateway.filters.SampleGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public SampleGlobalFilter sampleGlobalFilter() {
        return new SampleGlobalFilter();
    }


}
