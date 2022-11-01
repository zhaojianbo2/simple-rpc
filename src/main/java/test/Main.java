package test;

import com.alibaba.fastjson.JSONObject;

import netty.NettyClient;
import netty.handler.RpcResponseHandler;
import rpc.RpcClient;
import rpc.listener.JsonTaskFinishListener;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public class Main {

    public static void main(String[] args) {

	String name = "rpcServer1";
	String host = "127.0.0.1";
	int port = 8088;
	// 返回为json数据进行处理
	JsonTaskFinishListener taskFinishListener = new JsonTaskFinishListener();
	RpcResponseHandler businessHandler = new RpcResponseHandler(taskFinishListener);
	NettyClient<JSONObject> nettyClient = new NettyClient<JSONObject>(name, host, port, businessHandler);
	RpcClient.getInstance().registerConnection(nettyClient);
	long begin = System.currentTimeMillis();
	// String msg = RpcClient.getInstance().get(new PlayerMessage());
	// System.out.println(msg + " 耗时:" + (System.currentTimeMillis() - begin));
    }
}
