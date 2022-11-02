package rpc.rpctask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import lombok.Getter;
import lombok.Setter;

/**
 * 同步rpcTask
 * 
 * @author WinkeyZhao
 * @note
 *
 */
@Getter
@Setter
public class SyncRpcTask<T> extends AbstractRpcTask<T> {

    private Future<T> future;

    public SyncRpcTask(String taskId,Class<T> clazz) {
	super(taskId,clazz);
    }

    @Override
    public void run() {
	// 对不同future实例处理
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

    @Override
    public void cancel() {
	future.cancel(true);
    }
}
