package com.oneliang.frame.collector;

import com.oneliang.util.common.StringUtil;

public abstract class HttpCollector<T> implements Collector<String, T> {

    /**
     * replace all blank
     * 
     * @param text
     * @return String
     */
    protected String replaceAllBlank(String text) {
        text = text.replace(StringUtil.SPACE, StringUtil.BLANK)
                .replace(String.valueOf((char) StringUtil.CR), StringUtil.BLANK)
                .replace(String.valueOf((char) StringUtil.LF), StringUtil.BLANK)
                .replace(String.valueOf("\t"), StringUtil.BLANK)
                .replace(String.valueOf("\f"), StringUtil.BLANK)
                .replace(StringUtil.CRLF_STRING, StringUtil.BLANK)
                .replace("&nbsp;", StringUtil.BLANK);
        return text;
    }
}
