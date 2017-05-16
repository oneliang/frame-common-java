package com.oneliang.test.report;

import java.util.Map;

public class TestReporter implements Reporter{

    public void report(Map<String, Object> dataMap) {
        System.out.println(dataMap.get("1")+","+dataMap.get("2"));
    }

    public static void main(String[] args) {
        ReportManager.put("1", 1);
        ReportManager.put("2", 2);
        ReportManager.report(new TestReporter());
    }
}
