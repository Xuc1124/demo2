package com.ex.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xc on 2017/2/24.
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
    public int get(){
        return 1;
    }
}
