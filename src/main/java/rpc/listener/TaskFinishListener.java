package rpc.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import listener.ITaskFinishListener;
import message.rpcReq.AbstractMessage;
import rpc.rpctask.AbstractRpcTask;
import rpc.rpctask.AsyncRpcTask;
import rpc.rpctask.SyncRpcTask;

/**
 * 
 * @author WinkeyZhao
 * @note rpc任务收到回调监听 TODO 添加最大容量
 *
 */
public class TaskFinishListener implements ITaskFinishListener {

    private Map<String, AbstractRpcTask<?>> rpcTaskMap = new ConcurrentHashMap<>();

    /**
     * 添加RpcTask
     */
    @Override
    public void addRpcTask(AbstractRpcTask<?> rpcTask) {
	rpcTaskMap.put(rpcTask.getTaskId(), rpcTask);
    }

    /**
     * 同步完成
     */
    @Override
    public <T> T taskSyncFinish(SyncRpcTask<T> rpcTask) {
	System.out.println("taskFinish, taskId:" + rpcTask.getTaskId());
	// 否者返回接收到的数据
	return (T) rpcTask.getReturnData();
    }

    /**
     * 异步
     */
    @Override
    public <T> void taskAsyncFinish(AsyncRpcTask<T> asyncRpcTask) throws Exception {
	throw new Exception("此方法只能被异步调用");
    }

    /**
     * 超时
     */
    @Override
    public void timeOut(AbstractMessage msg) {
	String taskId = msg.getTaskId();
	System.out.println("tasktimeOut,taskMsg:" + msg);
	AbstractRpcTask<?> v = rpcTaskMap.remove(taskId);
	if (v == null) {
	    System.out.println("SncRpcTask == null taskId:" + taskId);
	}
	v.cancel();
    }

    @Override
    public AbstractRpcTask<?> getAndRemoveRpcTask(String taskId) {
	return rpcTaskMap.remove(taskId);
    }

    /**
     * 连接断开清理
     */
    @Override
    public void clear() {
	rpcTaskMap.values().forEach(e -> {
	    e.cancel();
	});
    }

}
