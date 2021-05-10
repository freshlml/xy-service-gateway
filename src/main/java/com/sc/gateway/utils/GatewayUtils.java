package com.sc.gateway.utils;

import org.springframework.context.ApplicationContext;

public class GatewayUtils {

    public static String getPropertyFormContext(ApplicationContext applicationContext, String key) {
        return applicationContext.getEnvironment().getProperty(key);
    }


}
