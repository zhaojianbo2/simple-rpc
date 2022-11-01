package netty.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import listener.ITaskFinishListener;
import rpc.RpcTask;

import java.util.concurrent.ExecutorService;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<String> {

    private ITaskFinishListener<JSONObject> taskFinishListener;
    private ExecutorService taskExecutors;

    public RpcResponseHandler(ExecutorService taskExecutors, ITaskFinishListener<JSONObject> taskFinishListener) {
	this.taskFinishListener = taskFinishListener;
	this.taskExecutors = taskExecutors;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
	JSONObject jsonObj = JSONObject.parseObject(s);
	String taskId = jsonObj.getString("taskId");
	// 直接移调rpcTask
	RpcTask<JSONObject> rpcTask = taskFinishListener.getAndRemoveRpcTask(taskId);
	if (rpcTask == null) {
	    System.out.println("没有找到RpcTask或者已经超时移除 taskId:" + taskId);
	    return;
	}
	// 赋值
	rpcTask.returnData = jsonObj;
	// 与netty线程脱离
	taskExecutors.submit(rpcTask);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    public ITaskFinishListener<?> getTaskFinishListener() {
	return taskFinishListener;
    }
}
