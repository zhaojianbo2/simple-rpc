package rpc;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * rpcTask
 * 
 * @author WinkeyZhao
 * @note
 *
 */
@Getter
public class RpcTask<T> implements Runnable {

    // rpcTask唯一标识
    private final String taskId;
    // rpcTask接收到的数据内容
    public T returnData;
    // future
    public Future<T> future;
    //同步rpcTask构造方法
    public RpcTask(String taskId) {
	this.taskId = taskId;
    }

    public T get(long time, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
	return future.get(time, timeUnit);
    }

    @Override
    public void run() {
	if (future instanceof FutureTask) {
	    // 设置回调数据
	    FutureTask<T> futureTask = (FutureTask<T>) future;
	    // 执行回调
	    futureTask.run();
	} else if (future instanceof CompletableFuture) {
	    CompletableFuture<T> completableFuture = (CompletableFuture<T>) future;
	    // completableFuture可以把值直接传递过去
	    completableFuture.complete(returnData);
	}
    }

    /**
     * 立刻取消task
     */
    public void cancel() {
	future.cancel(true);
    }
}
