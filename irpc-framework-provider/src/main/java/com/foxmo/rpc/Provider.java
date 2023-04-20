package com.foxmo.rpc;

import com.foxmo.rpc.entity.User;
import com.foxmo.rpc.protocol.URL;
import com.foxmo.rpc.protocol.socket.SocketProtocol;
import com.foxmo.rpc.register.LocalRegister;
import com.foxmo.rpc.service.BlogServiceImpl;
import com.foxmo.rpc.service.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class Provider {
    public static void main(String[] args) {
        //暴露实现类接口
        LocalRegister.regist(UserServiceImpl.class);
        LocalRegister.regist(BlogServiceImpl.class);

        SocketProtocol socketProtocol = new SocketProtocol();
        socketProtocol.start(new URL("127.0.0.1", 8899));
    }
}
