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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

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
	// 构造同步rpcTask
	RpcTask<T> rpcTask = new RpcTask<T>(taskId);
	// 使用 FutureTask 使用同步taskSyncFinish
	Future<T> future = createFutureTask(rpcTask, remoteImp, () -> {
	    return remoteImp.getTaskFinishListener().taskSyncFinish(rpcTask);
	});
	// 或者使用compltableFuture
	// Future<T> future = createCompletableFuture();

	rpcTask.future = future;
	try {
	    // 回去当前连接的TaskFinishListener 并添加rpcTask
	    remoteImp.getTaskFinishListener().addRpcTask(rpcTask);
	    // 远程客户端异步发送消息
	    remoteImp.sendMsg(taskMessageWrap);
	    // 同步等待结果
	    return rpcTask.get(time, timeUnit);
	} catch (InterruptedException | ExecutionException | TimeoutException e) {
	    e.printStackTrace();
	    // 超时处理
	    remoteImp.getTaskFinishListener().timeOut(taskMessageWrap);
	}
	return null;
    }

    /**
     * 异步task
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> void runAsync(IMessage message) {
	String taskId = UUID.randomUUID().toString();
	// 消息统一封装
	TaskMessageWrap taskMessageWrap = new TaskMessageWrap(taskId, message);
	// 这里随机要一个,演示用
	IRemoteImp<T> remoteImp = (IRemoteImp<T>) rpcConnectionList.get(random.nextInt(rpcConnectionList.size()));
	// 构建异步rpcTask
	AsyncRpcTask<T> asyncRpcTask = new AsyncRpcTask<T>(message.getMessageId(), null, taskId);
	// 使用compltableFuture
	// Future<T> future = createCompletableFuture();
	// 使用FutureTask 使用异步 这里返回值为null即可,异步不会调用future.get()
	FutureTask<T> future = createFutureTask(asyncRpcTask, remoteImp, () -> {
	    try {
		remoteImp.getTaskFinishListener().taskAsyncFinish(asyncRpcTask);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return null;
	});
	// 使用 FutureTask
	asyncRpcTask.future = future;
	// 回去当前连接的TaskFinishListener 并添加rpcTask
	remoteImp.getTaskFinishListener().addRpcTask(asyncRpcTask);
	// 远程客户端异步发送消息
	remoteImp.sendMsg(taskMessageWrap);
    }

    private <T> FutureTask<T> createFutureTask(RpcTask<T> rpcTask, IRemoteImp<T> remoteImp, Supplier<T> supplier) {
	FutureTask<T> future = new FutureTask<>(new Callable<T>() {
	    @Override
	    public T call() throws Exception {
		return supplier.get();
	    }
	});
	return future;
    }

    private <T> Future<T> createCompletableFuture() {
	CompletableFuture<T> future = new CompletableFuture<>();
	return future;
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
