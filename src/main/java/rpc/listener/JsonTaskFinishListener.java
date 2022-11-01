package rpc.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import com.alibaba.fastjson.JSONObject;

import listener.ITaskFinishListener;
import rpc.RpcTask;

/**
 * 
 * @author WinkeyZhao
 * @note rpc任务收到回调监听 TODO 添加最大容量
 *
 */
public class JsonTaskFinishListener implements ITaskFinishListener<JSONObject> {
    private Map<String, RpcTask<JSONObject>> rpcTaskMap = new ConcurrentHashMap<>();

    /**
     * 添加SncRpcTask
     * 
     * @param sncRpcTask
     */
    @Override
    public void addRpcTask(RpcTask<JSONObject> sncRpcTask) {
	rpcTaskMap.put(sncRpcTask.getTaskId(), sncRpcTask);
    }

    /**
     * 同步完成
     */
    @Override
    public JSONObject taskSyncFinish(String taskId) {
	System.out.println("taskFinish, taskId:" + taskId);
	RpcTask<JSONObject> v = rpcTaskMap.remove(taskId);
	if (v == null) {
	    System.out.println("SncRpcTask == null taskId:" + taskId);
	    return null;
	}
	// 否者返回接收到的数据
	return (JSONObject) v.returnData;
    }

    /**
     * 异步完成,这里要寻找处理线程保证顺序
     * TODO 
     */
    @Override
    public void taskAsyncFinish(String taskId) {
	System.out.println("taskFinish, taskId:" + taskId);
	RpcTask<JSONObject> v = rpcTaskMap.remove(taskId);
	if (v == null) {
	    System.out.println("SncRpcTask == null taskId:" + taskId);
	    return;
	}
    }

    /**
     * 超时
     */
    @Override
    public JSONObject timeOut(String taskId) {
	System.out.println("tasktimeOut,taskId:" + taskId);
	RpcTask<JSONObject> v = rpcTaskMap.remove(taskId);
	if (v == null) {
	    System.out.println("SncRpcTask == null taskId:" + taskId);
	}
	return null;
    }

    @Override
    public FutureTask<JSONObject> getFutureTask(String taskId) {
	return rpcTaskMap.get(taskId);
    }

    /**
     * 连接断开清理
     */
    @Override
    public void clear() {

    }

}
