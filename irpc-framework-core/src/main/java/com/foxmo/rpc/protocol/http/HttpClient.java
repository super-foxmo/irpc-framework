package com.foxmo.rpc.protocol.http;


import com.foxmo.rpc.RPCRequest;
import com.foxmo.rpc.RPCResponse;
import com.foxmo.rpc.register.remote.ServiceRegister;
import com.foxmo.rpc.register.remote.ZkServiceRegister;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {
    private static ServiceRegister serviceRegister;

    static {
        serviceRegister = new ZkServiceRegister();
    }

    public RPCResponse send(RPCRequest RPCRequest) {

        try {
            //从zookeeper注册中心获取服务端IP地址与接口
//            serviceRegister.serviceDiscovery(RPCRequest.getInterfaceName());
            URL url = new URL("http",
                    serviceRegister.serviceDiscovery(RPCRequest.getInterfaceName()).getHostname(),
                    serviceRegister.serviceDiscovery(RPCRequest.getInterfaceName()).getPort(),
                    "/");
            //创建连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);

            oos.writeObject(RPCRequest);
            oos.flush();
            oos.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            String result = IOUtils.toString(inputStream);

            return RPCResponse.success(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
