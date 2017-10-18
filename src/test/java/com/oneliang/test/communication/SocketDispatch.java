package com.oneliang.test.communication;

import java.io.InputStream;
import java.io.OutputStream;

public abstract interface SocketDispatch {

    /**
     * dispatch
     * 
     * @param inputStream
     * @param outputStream
     * @throws Exception
     */
    public abstract void dispatch(InputStream inputStream, OutputStream outputStream) throws Exception;
}
