package com.qbros.uithymeleaf;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@SessionScope
@Getter(AccessLevel.PRIVATE)
public class State implements Serializable {

    private final Map<String, Object> values = new HashMap<>();

    public Optional<Object> getValue(String key) {
        return Optional.ofNullable(values.get(key));
    }

    public void putValue(String key, Object subscription) {
        values.put(key, subscription);
    }
}
