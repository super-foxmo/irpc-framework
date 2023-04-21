package com.foxmo.rpc.proxy;

import com.foxmo.rpc.Invocation;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.protocol.URL;
import com.foxmo.rpc.protocol.netty.NettyProtocol;
import com.foxmo.rpc.protocol.socket.SocketClient;
import com.foxmo.rpc.protocol.socket.SocketProtocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientProxyFactory implements InvocationHandler {
    // 传入参数Service接口的class对象，反射封装成一个request
    private URL url;

    // jdk 动态代理， 每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Invocation的构建，使用了lombok中的builder，代码简洁
        Invocation invocation = Invocation.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes()).build();

        // Socket协议数据传输
//        SocketProtocol socketProtocol = new SocketProtocol();
//        RPCResponse response = socketProtocol.send(url, invocation);
        // Netty协议数据传输
        NettyProtocol nettyProtocol = new NettyProtocol();
        RPCResponse response = nettyProtocol.send(url, invocation);

        //System.out.println(response);
        return response.getData();
    }


    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}
