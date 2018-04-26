package com.quora.module;

import java.util.HashMap;
import java.util.Map;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/26 21:20
 * @version: 1.0
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
