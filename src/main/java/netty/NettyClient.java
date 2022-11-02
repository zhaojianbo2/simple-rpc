package netty;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;

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
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import listener.ITaskFinishListener;
import message.IMessage;
import message.rpcReq.AbstractMessage;
import netty.handler.RpcResponseHandler;
import rpc.imp.IRemoteImp;
import rpc.rpctask.AsyncRpcTask;
import rpc.rpctask.SyncRpcTask;

/**
 * rpc远程客户端netty实现
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public class NettyClient implements IRemoteImp {

    private Channel channel;

    /**
     * 创建nettyClient
     * 
     * @param name
     * @param host
     * @param port
     */
    public NettyClient(String name, String host, int port, RpcResponseHandler rpcResponseHandler) {
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
		// 回调rpc handler 绑定了对应的rpctask 所有这里不能sharable
		ch.pipeline().addLast("rpcResponseHandler", rpcResponseHandler);
	    }
	});
	try {
	    ChannelFuture f = b.connect(host, port).sync();
	    channel = f.channel();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public ITaskFinishListener getTaskFinishListener() {
	RpcResponseHandler rpcResponseHandler = (RpcResponseHandler) channel.pipeline().get("rpcResponseHandler");
	return rpcResponseHandler.getTaskFinishListener();
    }

    /**
     * 
     */
    @Override
    public <T> T get(AbstractMessage msg, SyncRpcTask<T> syncRpcTask, long time, TimeUnit timeUnit) {
	ChannelFuture channelFuture;
	try {
	    channelFuture = channel.writeAndFlush(JSON.toJSONString(msg)).sync();
	    // 确认消息发送,才阻塞等待
	    if (channelFuture.isSuccess()) {
		getTaskFinishListener().addRpcTask(syncRpcTask);
		return syncRpcTask.getFuture().get(time, timeUnit);
	    } else {
		System.out.println("netty 发送消息失败,直接返回null");
	    }
	} catch (InterruptedException | ExecutionException | TimeoutException e1) {
	    e1.printStackTrace();
	    getTaskFinishListener().timeOut(msg);
	}
	return null;
    }

    @Override
    public <T> void runAsync(AbstractMessage msg, AsyncRpcTask<T> AsyncRpcTask, long time, TimeUnit timeUnit) {
	channel.writeAndFlush(JSON.toJSONString(msg)).addListener(new GenericFutureListener<Future<? super Void>>() {
	    @Override
	    public void operationComplete(Future<? super Void> future) throws Exception {
		if (future.isSuccess()) {
		    getTaskFinishListener().addRpcTask(AsyncRpcTask);
		    CompletableFuture.delayedExecutor(time, timeUnit).execute(() -> {
			// 超时
			getTaskFinishListener().timeOut(msg);
		    });
		} else {
		    System.out.println("netty runAsync 发送消息失败");
		}
	    }
	});
    }
}
