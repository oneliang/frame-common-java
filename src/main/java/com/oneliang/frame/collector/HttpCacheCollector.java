package com.oneliang.frame.collector;

public abstract class HttpCacheCollector<T> implements Collector<HttpCacheCollector.From, T> {

    public static class From {
        public String httpUrl = null;
        public String cacheDirectory = null;

        public From(String httpUrl, String cacheDirectory) {
            this.httpUrl = httpUrl;
            this.cacheDirectory = cacheDirectory;
        }
    }
}
