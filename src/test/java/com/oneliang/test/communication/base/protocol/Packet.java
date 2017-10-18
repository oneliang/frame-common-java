package com.oneliang.test.communication.base.protocol;

import java.io.Serializable;

public abstract interface Packet extends Serializable {

    /**
     * to byte array
     * 
     * @return byte[]
     * @throws Exception
     */
    public abstract byte[] toByteArray() throws Exception;
}
