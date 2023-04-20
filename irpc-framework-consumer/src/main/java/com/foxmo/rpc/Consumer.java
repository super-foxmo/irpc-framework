package com.foxmo.rpc;

import com.foxmo.rpc.entity.Blog;
import com.foxmo.rpc.entity.User;
import com.foxmo.rpc.protocol.URL;
import com.foxmo.rpc.proxy.ClientProxyFactory;

public class Consumer {
    public static void main(String[] args) {
        ClientProxyFactory clientProxy = new ClientProxyFactory(new URL("127.0.0.1", 8899));

        UserService userService = clientProxy.getProxy(UserService.class);
        // 服务UserService的方法1
        User userByUserId = userService.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);
        // 服务UserService的方法2
        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userService.insertUserId(user);
        System.out.println("向服务端插入数据："+integer);

        // 服务BlogService的方法2
        BlogService blogService = clientProxy.getProxy(BlogService.class);
        Blog blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);



//        try {
//            // 建立Socket连接
//            Socket socket = new Socket("127.0.0.1", 8899);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            // 传给服务器id
//            objectOutputStream.writeInt(new Random().nextInt());
//            objectOutputStream.flush();
//            // 服务器查询数据，返回对应的对象
//            User user  = (User) objectInputStream.readObject();
//            System.out.println("服务端返回的User:"+user);
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            System.out.println("客户端启动失败");
//        }
    }
}
