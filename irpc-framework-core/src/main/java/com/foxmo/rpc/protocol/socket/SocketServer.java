package com.foxmo.rpc.protocol.socket;

import com.foxmo.rpc.protocol.RPCServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements RPCServer {
    @Override
    public void start(Integer port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动了");
            // BIO的方式监听Socket
            while (true){
                //接受请求
                Socket socket = serverSocket.accept();
                // 开启一个线程去处理接收到的请求
                new Thread(()->{
                    SocketServerHandler handler = new SocketServerHandler();
                    handler.handler(socket);
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    @Override
    public void close() {

    }
}
