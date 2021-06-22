package com.xy.gateway;

import com.sc.common.rmq.tx.service.impl.RmqTxServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class, FreeMarkerAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.xy", "com.sc"}, excludeFilters = {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = {RmqTxServiceImpl.class})})
public class GatewayApplication {

    public static void main(String[] argv) {
        SpringApplication.run(GatewayApplication.class, argv);
    }
}
