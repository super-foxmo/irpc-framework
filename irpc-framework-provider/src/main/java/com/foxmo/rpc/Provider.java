package com.foxmo.rpc;

import com.foxmo.rpc.protocol.URL;
import com.foxmo.rpc.protocol.netty.NettyProtocol;
import com.foxmo.rpc.register.local.LocalRegister;
import com.foxmo.rpc.register.remote.ServiceRegister;
import com.foxmo.rpc.register.remote.ZkServiceRegister;
import com.foxmo.rpc.service.BlogServiceImpl;
import com.foxmo.rpc.service.UserServiceImpl;

import java.net.InetSocketAddress;


public class Provider {
    public static void main(String[] args) {
        //暴露实现类接口
        LocalRegister.register(UserServiceImpl.class);
        LocalRegister.register(BlogServiceImpl.class);
        //将服务端的ip，端口注册到注册中心
        ServiceRegister serviceRegister = new ZkServiceRegister();
        serviceRegister.register(UserServiceImpl.class,new URL("127.0.0.1",8899));
        serviceRegister.register(BlogServiceImpl.class,new URL("127.0.0.1",8899));

        // Socket协议启动服务器
//        SocketProtocol socketProtocol = new SocketProtocol();
//        socketProtocol.start(new URL("127.0.0.1", 8899));

        // Netty协议启动服务器
        NettyProtocol nettyProtocol = new NettyProtocol();
        nettyProtocol.start(new URL("127.0.0.1",8899));
    }
}
