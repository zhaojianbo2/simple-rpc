package rpc;

import message.rpcReq.AbstractMessage;
import netty.NettyClient;
import register.IRpcConnectionRegister;
import rpc.imp.IRemoteImp;
import rpc.rpctask.AsyncRpcTask;
import rpc.rpctask.SyncRpcTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 
 * @author WinkeyZhao
 * @note RpcClient封装
 *
 */
public class RpcClient implements RpcTaskAction, IRpcConnectionRegister {

    // rpc远程具体实现
    private List<IRemoteImp> rpcConnectionList = new ArrayList<IRemoteImp>();
    // 随机id random
    private Random random = new Random();

    /**
     * rpc同步task
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(AbstractMessage message, long time, TimeUnit timeUnit) {
	// 这里随机要一个,演示用
	IRemoteImp remoteImp = rpcConnectionList.get(random.nextInt(rpcConnectionList.size()));
	Class<T> clazz = (Class<T>) message.getClazz();
	// 构造同步rpcTask
	SyncRpcTask<T> syncRpcTask = new SyncRpcTask<T>(message.getTaskId(),clazz);
	// 使用 FutureTask 使用同步taskSyncFinish
	FutureTask<T> future = createSyncFutureTask(syncRpcTask, remoteImp, () -> {
	    return remoteImp.getTaskFinishListener().taskSyncFinish(syncRpcTask);
	});
	// 或者使用compltableFuture
	// Future<T> future = createSyncCompletableFuture();
	syncRpcTask.setFuture(future);
	return remoteImp.get(message, syncRpcTask, time, timeUnit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void runAsync(AbstractMessage message, int backBindId, long backExcuteId, long time,
	    TimeUnit timeUnit) {
	// 这里随机要一个,演示用
	IRemoteImp remoteImp = rpcConnectionList.get(random.nextInt(rpcConnectionList.size()));
	Class<T> clazz = (Class<T>) message.getClazz();
	// 构建异步rpcTask
	AsyncRpcTask<T> asyncRpcTask = new AsyncRpcTask<T>(backBindId, backExcuteId, message.getTaskId(),
		remoteImp.getTaskFinishListener(),clazz);
	// 远程客户端异步发送消息
	remoteImp.runAsync(message, asyncRpcTask, time, timeUnit);
    }

    private <T> FutureTask<T> createSyncFutureTask(SyncRpcTask<T> rpcTask, IRemoteImp remoteImp, Supplier<T> supplier) {
	FutureTask<T> future = new FutureTask<>(new Callable<T>() {
	    @Override
	    public T call() throws Exception {
		return supplier.get();
	    }
	});
	return future;
    }

    private <T> Future<T> createSyncCompletableFuture() {
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
    public void registerConnection(NettyClient nettyClient) {
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
