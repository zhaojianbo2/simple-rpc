package rpc;

import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 同步rpcTask
 * 
 * @author WinkeyZhao
 * @note
 *
 */
@Getter
public class RpcTask<T> extends FutureTask<T> {

    private String taskId;
    public T returnData;

    public RpcTask(String taskId, Callable<T> callable) {
	super(callable);
	this.taskId = taskId;
    }

    public RpcTask(String taskId, Runnable runnable) {
	super(runnable, null);
	this.taskId = taskId;
    }
}
