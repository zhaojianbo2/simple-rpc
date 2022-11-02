package netty.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import listener.ITaskFinishListener;
import rpc.rpctask.AbstractRpcTask;

import java.util.concurrent.ExecutorService;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<String> {

    private ITaskFinishListener taskFinishListener;
    private ExecutorService taskExecutors;

    public RpcResponseHandler(ExecutorService taskExecutors, ITaskFinishListener taskFinishListener) {
	this.taskFinishListener = taskFinishListener;
	this.taskExecutors = taskExecutors;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
	JSONObject jsonObj = JSONObject.parseObject(s);
	// netty发送和接收是异步的所以 taskId也要带着回来.
	String taskId = jsonObj.getString("taskId");
	// 直接移调rpcTask
	AbstractRpcTask<?> rpcTask = taskFinishListener.getAndRemoveRpcTask(taskId);
	if (rpcTask == null) {
	    System.out.println("没有找到RpcTask或者已经超时移除 taskId:" + taskId);
	    return;
	}
	Class<?> clazz = rpcTask.getClazz();
	// 赋值
	rpcTask.setReturnData(jsonObj.toJavaObject(clazz));
	// 与netty线程脱离
	taskExecutors.submit(rpcTask);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    public ITaskFinishListener getTaskFinishListener() {
	return taskFinishListener;
    }
}
