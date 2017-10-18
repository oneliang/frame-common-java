package com.oneliang.test.communication.base.logic;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.oneliang.test.communication.base.protocol.TcpPacket;

public abstract interface BaseLogic extends Serializable {

    /**
     * send with byte array
     * 
     * @param outputStream
     * @param byteArray
     * @throws Exception
     */
    public abstract void send(OutputStream outputStream, byte[] byteArray) throws Exception;

    /**
     * send with type and body
     * 
     * @param outputStream
     * @param type
     * @param body
     * @throws Exception
     */
    public abstract void send(OutputStream outputStream, byte type, byte[] body) throws Exception;

    /**
     * send tcp packet
     * 
     * @param outputStream
     * @param tcpPacket
     * @throws Exception
     */
    public abstract void sendTcpPacket(OutputStream outputStream, TcpPacket tcpPacket) throws Exception;

    /**
     * receive
     * 
     * @param inputStream
     * @return byte[]
     * @throws Exception
     */
    public abstract byte[] receive(InputStream inputStream) throws Exception;

    /**
     * receive type
     * 
     * @param inputStream
     * @return byte
     * @throws Exception
     */
    public abstract byte receiveType(InputStream inputStream) throws Exception;

    /**
     * receive body,use next to receive type,short data packet use this method
     * 
     * @param inputStream
     * @return byte[]
     * @throws Exception
     */
    public abstract byte[] receiveBody(InputStream inputStream) throws Exception;

    /**
     * receive body,use next to receive type,long data packet use this method
     * 
     * @param inputStream
     * @param outputStream
     * @throws Exception
     */
    public abstract void receiveBody(InputStream inputStream, OutputStream outputStream) throws Exception;

    /**
     * receive tcp packet
     * 
     * @param inputStream
     * @return TcpPacket
     * @throws Exception
     */
    public abstract TcpPacket receiveTcpPacket(InputStream inputStream) throws Exception;
}
