package com.foxmo.rpc.protocol.netty;

import com.foxmo.rpc.RPCRequest;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.protocol.URL;
import com.foxmo.rpc.register.remote.ServiceRegister;
import com.foxmo.rpc.register.remote.ZkServiceRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

@Data
@NoArgsConstructor
public class NettyClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private static ServiceRegister serviceRegister;

    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
        serviceRegister = new ZkServiceRegister();
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回， 而不是想要的相应的response
     */
    public RPCResponse sendRequest(RPCRequest RPCRequest) {
        try {
            //从zookeeper注册中心获取服务端IP地址与接口
            URL url = serviceRegister.serviceDiscovery(RPCRequest.getInterfaceName());
            ChannelFuture channelFuture  = bootstrap.connect(url.getHostname(), url.getPort()).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(RPCRequest);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
