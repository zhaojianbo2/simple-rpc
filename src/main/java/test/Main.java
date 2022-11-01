package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;

import netty.NettyClient;
import netty.handler.RpcResponseHandler;
import rpc.RpcClient;
import rpc.listener.JsonTaskFinishListener;

/**
 * 
 * @author WinkeyZhao
 * @note TODO  进行测试
 *
 */
public class Main {

    public static void main(String[] args) {

	String name = "rpcServer1";
	String host = "127.0.0.1";
	int port = 8088;
	// 同步task
	JsonTaskFinishListener taskFinishListener = new JsonTaskFinishListener();
	//异步task 多了一个注册绑定msg处理器
	//AsyncJsonTaskFinishLisener taskFinishListener = new AsyncJsonTaskFinishLisener();
	//taskFinishListener.registerAsyncHandlerMap(1001, new Async1001Handler());
	
	//接收到netty之后的 脱离netty线程处理rpcTask
	ExecutorService taskExecutors = Executors.newCachedThreadPool();
	//netty channel handler
	RpcResponseHandler businessHandler = new RpcResponseHandler(taskExecutors, taskFinishListener);
	//构建nettyclient,IRemoteImp实现
	NettyClient<JSONObject> nettyClient = new NettyClient<JSONObject>(name, host, port, businessHandler);
	//rpc注册nettyClient
	RpcClient.getInstance().registerConnection(nettyClient);
    }
}
