package com.qbros.acs.personnel;

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
        xStream.allowTypesByWildcard(new String[]{"com.qbros.acs.api.**"});
        return xStream;
    }

    //Todo Apply better caching implementations
    @Bean
    public Cache personnelCache() {
        return new WeakReferenceCache();
    }
}
