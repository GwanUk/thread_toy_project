package com.matzip.thread.common.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("passwordEncoder")
public class PasswordEncoderFactoryBean implements FactoryBean<PasswordEncoder> {
    @Override
    public PasswordEncoder getObject() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public Class<?> getObjectType() {
        return PasswordEncoder.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
