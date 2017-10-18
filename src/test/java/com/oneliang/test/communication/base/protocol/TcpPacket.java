package com.oneliang.test.communication.base.protocol;

import java.io.ByteArrayOutputStream;

import com.oneliang.Constant;
import com.oneliang.util.common.MathUtil;

public class TcpPacket implements Packet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7004099559556262234L;

    public static final byte TYPE_NONE = -1;
    public static final byte TYPE_REQUEST_ADDRESS_BOOK = 0;
    public static final byte TYPE_REQUEST_SMS = 1;

    private byte type = TYPE_NONE;
    private byte[] body = null;

    public TcpPacket(byte type, byte[] body) {
        this.type = type;
        this.body = body;
    }

    public TcpPacket(byte type, String bodyString) {
        this.type = type;
        try {
            this.body = bodyString.getBytes(Constant.Encoding.UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBodyString() {
        String result = null;
        try {
            result = new String(this.body, Constant.Encoding.UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(type);
        if (body == null) {
            body = new byte[0];
        }
        byte[] bodyLengthByteArray = MathUtil.longToByteArray(body.length);
        byteArrayOutputStream.write(bodyLengthByteArray);
        byteArrayOutputStream.write(body);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(byte[] body) {
        this.body = body;
    }
}
