package com.qbros.acs.gate;

import com.thoughtworks.xstream.XStream;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigs {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{"com.qbros.acs.api.**", "com.qbros.acs.gate.internalcmd.**"});
        return xStream;
    }

    @Bean
    public Cache gateCache() {
        return new WeakReferenceCache();
    }
}
