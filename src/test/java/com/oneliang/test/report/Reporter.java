package com.oneliang.test.report;

import java.util.Map;

public abstract interface Reporter {

    /**
     * report
     * 
     * @param dataMap
     */
    public abstract void report(Map<String, Object> dataMap);
}
