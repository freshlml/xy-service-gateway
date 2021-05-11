package com.sc.gateway.config;

import com.sc.gateway.filters.SampleGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public SampleGlobalFilter sampleGlobalFilter() {
        return new SampleGlobalFilter();
    }


}
