package com.foxmo.rpc.protocol.socket;

import com.foxmo.rpc.RPCRequest;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.protocol.URL;
import com.foxmo.rpc.register.remote.ServiceRegister;
import com.foxmo.rpc.register.remote.ZkServiceRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
    // 这里负责底层与服务端的通信，发送的Request，接受的是Response对象
    // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
    // 这里的request是封装好的（上层进行封装），不同的service需要进行不同的封装， 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service

    private static ServiceRegister serviceRegister;

    static {
        serviceRegister = new ZkServiceRegister();
    }

    public static RPCResponse sendRequest(RPCRequest RPCRequest){
        try {
            //从zookeeper注册中心获取服务端IP地址与端口
            URL url = serviceRegister.serviceDiscovery(RPCRequest.getInterfaceName());
            Socket socket = new Socket(url.getHostname(), url.getPort());

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(RPCRequest);
            objectOutputStream.writeObject(RPCRequest);
            objectOutputStream.flush();

            RPCResponse response = (RPCResponse) objectInputStream.readObject();

            return response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }
    }
}
