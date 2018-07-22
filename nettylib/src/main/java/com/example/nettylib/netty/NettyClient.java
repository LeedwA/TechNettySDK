package com.example.nettylib.netty;


import com.example.nettylib.netty.base.Message;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * netty客户端
 *
 * @author lenovo
 */
public class NettyClient {
    static NettyClient nettyClient;
    String IP;
    String PORT;
    ConnectCallback connectCallback;
    ChannelInboundHandlerAdapter adapter;
    int time;//心跳间隔

    private NettyClient() {
    }

    public static NettyClient getInstance() {
        if (nettyClient == null) {
            nettyClient = new NettyClient();
        }
        return nettyClient;
    }

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient();
//        client.start(NettyPrams.IP, NettyPrams.PORT);
    }


    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//    private String HOST = "47.97.189.126";
//    private final static int PORT = 8099;

    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;

    /****************************************
     方法描述： 启动netty
     @param host
     @param port
     @param time  心跳时间间隔（单位秒）
     @return
     ****************************************/
    public void start(String host,
                      String port,
                      final int time,
                      final ConnectCallback connectCallback,
                      final ChannelInboundHandlerAdapter adapter) {
        if (NettyClient.getInstance().isChannerAlive())
            NettyClient.getInstance().disConect();
        start(host, port, time, false, connectCallback, adapter);
    }

    /****************************************
     方法描述： 启动netty
     @param host
     @param port
     @param time  心跳时间间隔（单位秒）

     @return
     ****************************************/
    public void start(String host,
                      String port,
                      final int time,
                      boolean isForce,
                      final ConnectCallback connectCallback,
                      final ChannelInboundHandlerAdapter adapter) {
        if (isForce) {
            disConect();
        }
        if (isChannerAlive()) return;
        this.IP = host;
        this.PORT = port;
        this.time = time;
        this.connectCallback = connectCallback;
        this.adapter = adapter;

        System.out.println("开始连接");
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //心跳
                            socketChannel.pipeline().addLast(new IdleStateHandler(time, time, time, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            socketChannel.pipeline().addLast(new ProtobufDecoder(Message.Head.getDefaultInstance()));
                            socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            socketChannel.pipeline().addLast(new ProtobufEncoder());
//                            socketChannel.pipeline().addLast(new CommonClientHandler(handleCallback, ecrikey));
                            socketChannel.pipeline().addLast(adapter);
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            super.exceptionCaught(ctx, cause);
//                            start(IP, PORT, time, connectCallback, adapter);
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true);
//            doConnect();
            final ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, Integer.parseInt(port)));
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    channel = channelFuture.channel();
                    if (future.isSuccess()) {
                        System.out.println("Client 连接成功-->>>>>>");
                        connectCallback.isConnect(true);
                    } else {
                        System.out.println("Client 连接失败-->>>>>>");
                        reConnect();
                    }
                }
            });
            future.channel().closeFuture();//这一步会阻塞住
            System.out.println("关闭后");
        } catch (Exception e) {
            reConnect();
        }
    }

    //重新连接
    public synchronized void reConnect() {
        if (connectCallback == null) return;
        connectCallback.isConnect(false);
        disConect();
        //断错重连
        executor.execute(new Runnable() {
            public void run() {
                System.out.println("Client 尝试重新连接-->>>>>>");
                //等待InterVAl时间，重连
                try {
                    TimeUnit.SECONDS.sleep(5);
                    //发起重连
                    start(IP, PORT, time, connectCallback, adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface ConnectCallback {
        //netty是否连接成功
        void isConnect(boolean isConnect);
    }

    //断开连接
    public void disConect() {
        if (channel != null) {
            channel.closeFuture();
            channel=null;
        }
    }

    int i;


    public boolean sendMsg(Message.Head msg) {
        if (isChannerAlive()) {
            boolean issucess = channel.writeAndFlush(msg).isSuccess();
            System.out.println("sended message" + (issucess ? (++i) : i));
            return issucess;
        } else {
            System.out.println("sendfail message");
            return false;
        }
    }

    //链接是否有效
    public boolean isChannerAlive() {
        return channel != null && channel.isActive();
    }


}
