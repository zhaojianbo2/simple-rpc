package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

import message.rpcReq.RpcCommonMsg;
import netty.NettyClient;
import netty.handler.RpcResponseHandler;
import rpc.RpcClient;
import rpc.asyncHandler.Async1001Handler;
import rpc.listener.AsyncTaskFinishLisener;
import rpc.listener.TaskFinishListener;
import rpc.proxy.RPCClientProxy;
import rpc.remoteHandler.LogicService;
import rpc.remoteHandler.User;

/**
 * 
 * @author WinkeyZhao
 * @note TODO 进行测试
 *
 */
public class Main {

    public static void main(String[] args) {

	String name = "rpcServer1";
	String host = "127.0.0.1";
	int port = 8088;
	// 同步task
	TaskFinishListener taskFinishListener = new TaskFinishListener();
	// 接收到netty之后的 脱离netty线程处理rpcTask
	ExecutorService taskExecutors = Executors.newCachedThreadPool();
	// netty channel handler
	RpcResponseHandler businessHandler = new RpcResponseHandler(taskExecutors, taskFinishListener);
	// 构建nettyclient,IRemoteImp实现
	NettyClient nettyClient = new NettyClient(name, host, port, businessHandler);
	// rpc注册nettyClient
	RpcClient.getInstance().registerConnection(nettyClient);
	RpcCommonMsg msg = new RpcCommonMsg(1000, JSONObject.class);
	// 同步获取
	RpcClient.getInstance().get(msg, 10, TimeUnit.SECONDS);

	// ---------------------分割线----------------------------------
	// 同步动态代理
	RPCClientProxy rPCClientProxy = new RPCClientProxy(msg, 10, TimeUnit.SECONDS);
	LogicService logicService = rPCClientProxy.getProxy(LogicService.class);
	User user = logicService.getUser();

	// ---------------------分割线----------------------------------
	// 异步双通,服务器也可以主动推送信息过来
	AsyncTaskFinishLisener asyncTaskFinishLisener = new AsyncTaskFinishLisener();
	asyncTaskFinishLisener.registerAsyncHandlerMap(2000, new Async1001Handler());
	// 接收到netty之后的 脱离netty线程处理rpcTask
	ExecutorService taskExecutors2 = Executors.newCachedThreadPool();
	// netty channel handler
	RpcResponseHandler businessHandler2 = new RpcResponseHandler(taskExecutors2, asyncTaskFinishLisener);
	// 构建nettyclient,IRemoteImp实现
	NettyClient nettyClient2 = new NettyClient(name, host, port, businessHandler2);
	// rpc注册nettyClient
	RpcClient.getInstance().registerConnection(nettyClient2);
	RpcCommonMsg msg2 = new RpcCommonMsg(1000, JSONObject.class);
	// 异步获取
	RpcClient.getInstance().runAsync(msg2, 2000, 1, 1, TimeUnit.SECONDS);

    }
}
