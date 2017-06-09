package com.oneliang.frame.collector;

public abstract interface Collector<FROM, DATA> {

    /**
     * collect
     * 
     * @param from
     * @return DATA
     * @throws Exception
     */
    public abstract DATA collect(FROM from) throws Exception;
}
