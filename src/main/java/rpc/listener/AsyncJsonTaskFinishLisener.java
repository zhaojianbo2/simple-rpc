package rpc.listener;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import rpc.AsyncRpcTask;
import rpc.asyncHandler.AsyncBaseHandler;

/**
 * 异步rpcTask监听,多了一个消息绑定,所以直接继承同步
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public class AsyncJsonTaskFinishLisener extends JsonTaskFinishListener {

    private Map<Integer, AsyncBaseHandler> asyncHandlerMap = new HashMap<>();

    public void registerAsyncHandlerMap(int msgId, AsyncBaseHandler handler) {
	asyncHandlerMap.put(msgId, handler);
    }

    /**
     * 异步完成,这里要寻找处理线程保证顺序
     */
    public void taskAsyncFinish(AsyncRpcTask<JSONObject> asyncRpcTask) {
	// 指定线程执行
	if (asyncRpcTask.getExcutor() != null) {
	    asyncRpcTask.getExcutor().execute(() -> {
		AsyncBaseHandler handler = asyncHandlerMap.get(asyncRpcTask.getMsgId());
		handler.action();
	    });
	} else {
	    // 直接执行
	    AsyncBaseHandler handler = asyncHandlerMap.get(asyncRpcTask.getMsgId());
	    handler.action();
	}

    }
}
