package netty.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import listener.ITaskFinishListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
@Sharable
public class RpcResponseHandler extends SimpleChannelInboundHandler<String> {

    private ITaskFinishListener<?> taskFinishListener;
    private ExecutorService taskExecutors;

    public RpcResponseHandler(ITaskFinishListener<?> taskFinishListener) {
	this.taskFinishListener = taskFinishListener;
	taskExecutors = Executors.newCachedThreadPool();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
	JSONObject SONObject = JSONObject.parseObject(s);
	String taskId = SONObject.getString("taskId");
	// 获取FutureTask,并提交到线程池当中执行
	FutureTask<?> rpcTask = taskFinishListener.getFutureTask(taskId);
	taskExecutors.submit(rpcTask);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    public ITaskFinishListener<?> getTaskFinishListener() {
	return taskFinishListener;
    }
}
