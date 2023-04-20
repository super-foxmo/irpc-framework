package com.foxmo.rpc.protocol.socket;

import com.foxmo.rpc.protocol.RPCServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolSocketServer implements RPCServer {
    private final ThreadPoolExecutor threadPool;

    public ThreadPoolSocketServer(){
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }
    public ThreadPoolSocketServer(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue){

        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 启动服务端
     * @param port 端口号
     */
    @Override
    public void start(Integer port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动了");
            // BIO的方式监听Socket
            while (true){
                Socket socket = serverSocket.accept();
                // 从线程池中获取一个线程去处理接收到的请求
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        new SocketServerHandler().handler(socket);
                    }
                });
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
