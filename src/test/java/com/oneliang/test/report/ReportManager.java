package com.oneliang.test.report;

import java.util.HashMap;
import java.util.Map;

/**
 * only support same thread
 * 
 * @author oneliang
 */
public class ReportManager {

    private static ThreadLocal<Map<String, Object>> threadLocalMap = new ThreadLocal<Map<String, Object>>() {
        protected java.util.Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        };
    };

    public static void put(String key, Object value) {
        threadLocalMap.get().put(key, value);
    }

    public static void report(Reporter reporter) {
        if (reporter != null) {
            Map<String, Object> dataMap = threadLocalMap.get();
            reporter.report(dataMap);
            threadLocalMap.remove();
        }
    }
}
