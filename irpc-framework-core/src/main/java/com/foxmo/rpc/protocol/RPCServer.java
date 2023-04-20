package com.foxmo.rpc.protocol;

public interface RPCServer {
    void start(Integer port);
    void close();
}
