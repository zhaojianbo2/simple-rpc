package listener;

import java.util.concurrent.FutureTask;

import rpc.RpcTask;

/**
 * 
 * @author WinkeyZhao
 * @note 收到netty回调信息后的监听
 *
 */
public interface ITaskFinishListener<T> {

    public T taskSyncFinish(String taskId);
    
    public void taskAsyncFinish(String taskId);

    public T timeOut(String taskId);
    
    public void addRpcTask(RpcTask<T> sncRpcTask);

    public FutureTask<T> getFutureTask(String taskId);
    
    public void clear();

}
