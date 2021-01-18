package com.maltsev.scheduler.factory;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@AllArgsConstructor
public class GenericFactory<K, V> {
    private final Map<K, V> keyValueMap = new HashMap<>();

    private Function<K, V> defaultValueFunction;

    public void register(K key, V value) {
        keyValueMap.put(key, value);
    }

    public V get(K key) {
        V value = keyValueMap.get(key);

        if (Objects.isNull(value)) {
            return defaultValueFunction.apply(key);
        }

        return value;
    }
}
