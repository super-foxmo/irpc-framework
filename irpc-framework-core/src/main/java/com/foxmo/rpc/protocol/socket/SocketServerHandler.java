package com.foxmo.rpc.protocol.socket;

import com.foxmo.rpc.RPCRequest;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.register.LocalRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SocketServerHandler {
    public void handler(Socket socket){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

//                        // 读取客户端传过来的id
//                        Integer id = ois.readInt();
//                        User userByUserId = userService.getUserByUserId(id);
//                        // 写入User对象给客户端
//                        oos.writeObject(userByUserId);

            // 读取客户端传过来的invocation
            RPCRequest RPCRequest = (RPCRequest) ois.readObject();
            // 反射调用对应方法
            //获取调用方法的实现类
            Class clazz = LocalRegister.get(RPCRequest.getInterfaceName());
            //获取调用方法
            Method method = clazz.getMethod(RPCRequest.getMethodName(), RPCRequest.getParamsTypes());
            //运行调用方法
            Object result = method.invoke(clazz.newInstance(), RPCRequest.getParams());
            //封装响应类
            oos.writeObject(RPCResponse.success(result));
            oos.flush();

        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("从IO中读取数据错误");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
