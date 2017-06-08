package com.oneliang.frame.collector;

import java.util.List;

public abstract interface Collector<FROM, DATA> {

    /**
     * collect
     * 
     * @param from
     * @return List<DATA>
     * @throws Exception
     */
    public abstract List<DATA> collect(FROM from) throws Exception;
}
