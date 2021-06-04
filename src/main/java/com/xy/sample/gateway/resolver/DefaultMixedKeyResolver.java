package com.xy.sample.gateway.resolver;

public class DefaultMixedKeyResolver extends MixedKeyResolver {

    @Override
    protected void addCustomKeyResolvers() {
        addToKeyResolvers(new AddrKeyResolver());
        addToKeyResolvers(new RequestUserKeyResolver());
        addToKeyResolvers(new NoQualifierKeyResolver());

        addToKeyResolvers(new PrincipalNameKeyResolverAdapter()); //设置the last default KeyResolver，也可以不设置
    }
}
