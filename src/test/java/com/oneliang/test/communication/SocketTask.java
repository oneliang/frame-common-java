package com.oneliang.test.communication;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.oneliang.test.communication.base.logic.BaseLogic;
import com.oneliang.test.communication.base.logic.impl.BaseLogicImpl;
import com.oneliang.util.concurrent.ThreadTask;

public class SocketTask implements ThreadTask {

    private Socket socket = null;
    private BaseLogic baseLogic = new BaseLogicImpl();

    public SocketTask(Socket socket) {
        this.socket = socket;
    }

    public void runTask() {
        try {
            InputStream inputStream = this.socket.getInputStream();
            OutputStream outputStream = this.socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
