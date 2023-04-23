package com.foxmo.rpc.protocol.netty;

import com.foxmo.rpc.RPCRequest;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.protocol.Protocol;
import com.foxmo.rpc.protocol.URL;

public class NettyProtocol implements Protocol {
    @Override
    public void start(URL url) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(url.getPort());
    }

    @Override
    public RPCResponse send(RPCRequest RPCRequest) {
        NettyClient nettyClient = new NettyClient();
        return nettyClient.sendRequest(RPCRequest);
    }
}
