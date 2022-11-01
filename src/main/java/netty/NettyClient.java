package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import listener.ITaskFinishListener;
import netty.handler.RpcResponseHandler;
import rpc.TaskMessageWrap;
import rpc.imp.IRemoteImp;

/**
 * rpc远程客户端netty实现
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public class NettyClient<T> implements IRemoteImp<T> {

    private RpcResponseHandler rpcResponseHandler;
    private Channel channel;

    /**
     * 创建nettyClient
     * 
     * @param name
     * @param host
     * @param port
     */
    public NettyClient(String name, String host, int port, RpcResponseHandler rpcResponseHandler) {
	this.rpcResponseHandler = rpcResponseHandler;
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	Bootstrap b = new Bootstrap(); // (1)
	b.group(workerGroup); // (2)
	b.channel(NioSocketChannel.class); // (3)
	b.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.SO_REUSEADDR, true); // (4)
	b.handler(new ChannelInitializer<SocketChannel>() {
	    @Override
	    public void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new StringEncoder());
		// 单列handler
		ch.pipeline().addLast(rpcResponseHandler);
	    }
	});
	try {
	    ChannelFuture f = b.connect(host, port).sync();
	    channel = f.channel();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    @SuppressWarnings("unchecked")
    public ITaskFinishListener<T> getTaskFinishListener() {
	return (ITaskFinishListener<T>) rpcResponseHandler.getTaskFinishListener();
    }

    /**
     * netty 异步发送消息
     */
    @Override
    public void sendMsg(TaskMessageWrap taskMessageWrap) {
	channel.writeAndFlush(taskMessageWrap.toString());
    }
}
