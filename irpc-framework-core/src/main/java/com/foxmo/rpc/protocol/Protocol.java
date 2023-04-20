package com.foxmo.rpc.protocol;

import com.foxmo.rpc.Invocation;
import com.foxmo.rpc.RPCResponse;

public interface Protocol {
    void start(URL url);
    RPCResponse send(URL url, Invocation invocation);
}
