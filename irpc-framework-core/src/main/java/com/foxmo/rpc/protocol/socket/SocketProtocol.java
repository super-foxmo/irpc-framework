package com.foxmo.rpc.protocol.socket;

import com.foxmo.rpc.RPCRequest;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.protocol.Protocol;
import com.foxmo.rpc.protocol.URL;

public class SocketProtocol implements Protocol {
    @Override
    public void start(URL url) {
//        SocketServer socketServer = new SocketServer();
//        socketServer.start(url.getPort());

        //使用ThreadPoolSocketServer
        ThreadPoolSocketServer threadPoolSocketServer = new ThreadPoolSocketServer();
        threadPoolSocketServer.start(url.getPort());
    }

    @Override
    public RPCResponse send(URL url, RPCRequest RPCRequest) {
        return SocketClient.sendRequest(url.getHostname(),url.getPort(), RPCRequest);
    }
}
