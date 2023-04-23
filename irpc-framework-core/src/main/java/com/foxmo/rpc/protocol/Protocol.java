package com.foxmo.rpc.protocol;

import com.foxmo.rpc.RPCRequest;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.register.remote.ServiceRegister;
import com.foxmo.rpc.register.remote.ZkServiceRegister;

public interface Protocol {

    void start(URL url);
    RPCResponse send(RPCRequest RPCRequest);
}
