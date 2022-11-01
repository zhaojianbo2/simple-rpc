package rpc;

import message.IMessage;
import netty.NettyClient;
import register.IRpcConnectionRegister;
import rpc.imp.IRemoteImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author WinkeyZhao
 * @note RpcClient封装
 *
 */
public class RpcClient implements RpcTaskAction, IRpcConnectionRegister {

    // rpc远程具体实现
    private List<IRemoteImp<?>> rpcConnectionList = new ArrayList<IRemoteImp<?>>();
    // 随机id random
    private Random random = new Random();

    /**
     * rpc同步task
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(IMessage message, long time, TimeUnit timeUnit) {
	String taskId = UUID.randomUUID().toString();
	// 消息统一封装
	TaskMessageWrap taskMessageWrap = new TaskMessageWrap(taskId, message);
	// 这里随机要一个,演示用
	IRemoteImp<T> remoteImp = (IRemoteImp<T>) rpcConnectionList.get(random.nextInt(rpcConnectionList.size()));
	RpcTask<T> rpcTask = new RpcTask<T>(taskId, new Callable<T>() {
	    @Override
	    public T call() throws Exception {
		// 正常收到消息返回处理
		return remoteImp.getTaskFinishListener().taskSyncFinish(taskId);
	    }
	});
	try {
	    // 加入回调map
	    remoteImp.getTaskFinishListener().addRpcTask(rpcTask);
	    // 远程客户端异步发送消息
	    remoteImp.sendMsg(taskMessageWrap);
	    // 同步等待结果
	    return rpcTask.get(time, timeUnit);
	} catch (InterruptedException | ExecutionException | TimeoutException e) {
	    e.printStackTrace();
	    // 超时处理
	    remoteImp.getTaskFinishListener().timeOut(taskId);
	}
	return null;
    }

    /**
     * 异步task
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> void getAsync(IMessage message) {
	String taskId = UUID.randomUUID().toString();
	// 消息统一封装
	TaskMessageWrap taskMessageWrap = new TaskMessageWrap(taskId, message);
	// 这里随机要一个,演示用
	IRemoteImp<T> remoteImp = (IRemoteImp<T>) rpcConnectionList.get(random.nextInt(rpcConnectionList.size()));
	RpcTask<T> rpcTask = new RpcTask<T>(taskId, new Runnable() {
	    @Override
	    public void run() {
		remoteImp.getTaskFinishListener().taskAsyncFinish(taskId);
	    }
	});
	// 加入回调map
	remoteImp.getTaskFinishListener().addRpcTask(rpcTask);
	// 远程客户端异步发送消息
	remoteImp.sendMsg(taskMessageWrap);

    }

    /**
     * 注册rpc远程通信客户端
     *
     * @param name
     * @param host
     * @param port
     */
    @Override
    public <T> void registerConnection(NettyClient<T> nettyClient) {
	rpcConnectionList.add(nettyClient);
    }

    private RpcClient() {
    }

    private static class Holder {
	static RpcClient client = new RpcClient();
    }

    public static RpcClient getInstance() {
	return Holder.client;
    }

}
