package com.qbros.acs.reports.configs;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /*
     * https://discuss.axoniq.io/t/getting-xstream-dependency-exception/3634
     */
    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{"com.qbros.acs.api.**"});
        return xStream;
    }
}
