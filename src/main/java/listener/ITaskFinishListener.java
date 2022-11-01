package listener;


import rpc.AsyncRpcTask;
import rpc.RpcTask;
import rpc.TaskMessageWrap;

/**
 * 
 * @author WinkeyZhao
 * @note 收到netty回调信息后的监听
 *
 */
public interface ITaskFinishListener<T> {

    public T taskSyncFinish(RpcTask<T> rpcTask);

    public void taskAsyncFinish (AsyncRpcTask<T> asyncRpcTask) throws Exception;

    public void timeOut(TaskMessageWrap taskMessageWrap);

    public void addRpcTask(RpcTask<T> sncRpcTask);

    public RpcTask<T> getAndRemoveRpcTask(String taskId);

    public void clear();

}
