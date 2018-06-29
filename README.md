# Netty网络请求框架
```
    初始化：启动
    
    NettyClient.getInstance().start("118.25.xx.xx", "8099", 1000, new NettyClient.ConnectCallback() {
            @Override
            public void isConnect(boolean isConnect) {

            }
        }, new TestAdapter());
```
