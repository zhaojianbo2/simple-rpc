package test.rpcserver;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zGame
 * @version V1.0
 * @Project: simple-rpc
 * @Package test.rpcserver
 * @Description: TODO
 * @date Date : 2022年02月28日 14:24
 */
public class server {
    
    public static void main(String[] args) {
    
        /**
         * 注册处理rpcClient的handler
         */
        Map<Integer,RpcActon> rpcActonMap = new HashMap<>();
        rpcActonMap.put(1001,new RpcServerHandler());
        
        //启动nettyserver
        Thread t = new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ChannelFuture f = null; //
            try {
                ServerBootstrap b = new ServerBootstrap(); // (2)
                b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
                        .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new StringDecoder());
                                ch.pipeline().addLast(new StringEncoder());
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
                                            throws Exception {
                                        JSONObject jsonObject = JSONObject.parseObject(s);
                                        JSONObject message = jsonObject.getJSONObject("message");
                                        int messageId = message.getInteger("messageId");
                                        RpcActon handler = rpcActonMap.get(messageId);
                                        handler.action(channelHandlerContext,jsonObject);
                                    }
                                });
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128)
                        .option(ChannelOption.SO_REUSEADDR, true)// (5)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                
                try {
                    f = b.bind("127.0.0.1", 8088).sync();
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        });
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}
