package rpc.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;

import listener.ITaskFinishListener;
import rpc.AsyncRpcTask;
import rpc.RpcTask;
import rpc.TaskMessageWrap;

/**
 * 
 * @author WinkeyZhao
 * @note rpc任务收到回调监听 TODO 添加最大容量
 *
 */
public class JsonTaskFinishListener implements ITaskFinishListener<JSONObject> {

    private Map<String, RpcTask<JSONObject>> rpcTaskMap = new ConcurrentHashMap<>();

    /**
     * 添加RpcTask
     */
    @Override
    public void addRpcTask(RpcTask<JSONObject> sncRpcTask) {
	rpcTaskMap.put(sncRpcTask.getTaskId(), sncRpcTask);
    }

    /**
     * 同步完成
     */
    @Override
    public JSONObject taskSyncFinish(RpcTask<JSONObject> rpcTask) {
	System.out.println("taskFinish, taskId:" + rpcTask.getTaskId());
	// 否者返回接收到的数据
	return rpcTask.returnData;
    }

    /**
     * 异步
     */
    @Override
    public void taskAsyncFinish(AsyncRpcTask<JSONObject> asyncRpcTask) throws Exception {
	throw new Exception("此方法只能被异步调用");
    }

    /**
     * 超时
     */
    @Override
    public void timeOut(TaskMessageWrap taskMessageWrap) {
	String taskId = taskMessageWrap.getTaskId();
	System.out.println("tasktimeOut,taskMsg:" + taskMessageWrap);
	RpcTask<JSONObject> v = rpcTaskMap.remove(taskId);
	if (v == null) {
	    System.out.println("SncRpcTask == null taskId:" + taskId);
	}
    }

    @Override
    public RpcTask<JSONObject> getAndRemoveRpcTask(String taskId) {
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
