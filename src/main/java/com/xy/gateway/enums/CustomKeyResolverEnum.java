package com.xy.gateway.enums;


import java.util.Arrays;

public enum CustomKeyResolverEnum {
    ADDR("ADDR"),
    USER_SIGN("USER_SIGN"),
    NO_QUALIFIER("NO_QUALIFIER");

    private String value;

    CustomKeyResolverEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CustomKeyResolverEnum getByValue(String value) {
        return Arrays.stream(CustomKeyResolverEnum.values()).filter(scan -> scan.getValue().equals(value)).findFirst().orElse(null);
    }

}
