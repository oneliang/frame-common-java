package com.oneliang.test.communication;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oneliang.util.concurrent.ThreadPool;
import com.oneliang.util.socket.SocketPool;

public class Server {
    Map<String, SocketPool> socketPoolMap = new ConcurrentHashMap<>();

    public void execute() throws Exception {
        ThreadPool threadPool = new ThreadPool();
        threadPool.setMinThreads(1);
        threadPool.setMaxThreads(20);
        try {
            threadPool.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket);
            threadPool.addThreadTask(new SocketTask(socket));
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().execute();
    }
}
