package com.oneliang.test.communication.base.logic.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.oneliang.Constant;
import com.oneliang.frame.ioc.Ioc;
import com.oneliang.test.communication.base.logic.BaseLogic;
import com.oneliang.test.communication.base.protocol.TcpPacket;
import com.oneliang.util.common.MathUtil;

@Ioc
public class BaseLogicImpl implements BaseLogic {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7463382376166500888L;

    /**
     * send
     * 
     * @param outputStream
     * @param byteArray
     * @throws Exception
     */
    public void send(OutputStream outputStream, byte[] byteArray) throws Exception {
        if (byteArray != null) {
            int totalLength = byteArray.length;
            int offset = 0;
            int last = totalLength;
            while (last > 0) {
                if (last >= Constant.Capacity.BYTES_PER_KB) {
                    outputStream.write(byteArray, offset, Constant.Capacity.BYTES_PER_KB);
                    outputStream.flush();
                    offset += Constant.Capacity.BYTES_PER_KB;
                    last = totalLength - offset;
                } else {
                    outputStream.write(byteArray, offset, last);
                    outputStream.flush();
                    offset += last;
                    last = totalLength - offset;
                }
            }
        }
    }

    /**
     * send with type and body
     * 
     * @param outputStream
     * @param type
     * @param body
     * @throws Exception
     */
    public void send(OutputStream outputStream, byte type, byte[] body) throws Exception {
        sendTcpPacket(outputStream, new TcpPacket(type, body));
    }

    /**
     * send tcp packet
     * 
     * @param outputStream
     * @param tcpPacket
     * @throws Exception
     */
    public void sendTcpPacket(OutputStream outputStream, TcpPacket tcpPacket) throws Exception {
        if (tcpPacket != null) {
            send(outputStream, tcpPacket.toByteArray());
        }
    }

    /**
     * receive
     * 
     * @param inputStream
     * @return byte[]
     * @throws Exception
     */
    public byte[] receive(InputStream inputStream) throws Exception {
        return this.receiveTcpPacket(inputStream).toByteArray();
    }

    /**
     * receive type
     * 
     * @param inputStream
     * @return byte
     * @throws Exception
     */
    public byte receiveType(InputStream inputStream) throws Exception {
        return (byte) inputStream.read();
    }

    /**
     * receive body,use next to receive type,short data packet use this method
     * 
     * @param inputStream
     * @return byte[]
     * @throws Exception
     */
    public byte[] receiveBody(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
        this.receiveBody(inputStream, bodyOutputStream);
        return bodyOutputStream.toByteArray();
    }

    /**
     * receive body,use next to receive type,long data packet use this method
     * 
     * @param inputStream
     * @param outputStream
     * @throws Exception
     */
    public void receiveBody(InputStream inputStream, OutputStream outputStream) throws Exception {
        if (outputStream != null) {
            byte[] bodyLengthByteArray = new byte[8];
            inputStream.read(bodyLengthByteArray, 0, bodyLengthByteArray.length);
            long bodyLength = MathUtil.byteArrayToLong(bodyLengthByteArray);
            byte[] buffer = new byte[Constant.Capacity.BYTES_PER_KB];
            long last = bodyLength;
            while (last > 0) {
                int length = inputStream.read(buffer, 0, buffer.length);
                if (length > 0) {
                    outputStream.write(buffer, 0, length);
                    outputStream.flush();
                    last = last - length;
                }
            }
        }
    }

    /**
     * receive tcp packet
     * 
     * @param inputStream
     * @return TcpPacket
     * @throws Exception
     */
    public TcpPacket receiveTcpPacket(InputStream inputStream) throws Exception {
        byte type = this.receiveType(inputStream);
        byte[] bodyByteArray = this.receiveBody(inputStream);
        return new TcpPacket(type, bodyByteArray);
    }
}
