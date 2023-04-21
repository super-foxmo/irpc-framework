package com.foxmo.rpc.protocol.netty;

import com.foxmo.rpc.Invocation;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.register.LocalRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 因为是服务器端，我们知道接受到请求格式是RPCinvocation
 * Object类型也行，强制转型就行
 */
@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<Invocation> {
//    private ServiceProvider serviceProvider;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation msg) throws Exception {
        //System.out.println(msg);
        RPCResponse response = getResponse(msg);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    RPCResponse getResponse(Invocation invocation) {
        // 得到服务名
        String interfaceName = invocation.getInterfaceName();
        // 得到服务端相应服务实现类
        Class clazz = LocalRegister.get(interfaceName);
//        Object service = serviceProvider.getService(interfaceName);
        // 反射调用方法
        Method method = null;
        try {
            method = clazz.getMethod(invocation.getMethodName(), invocation.getParamsTypes());
            Object invoke = method.invoke(clazz.newInstance(), invocation.getParams());
            return RPCResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RPCResponse.fail();
        }
    }
}
