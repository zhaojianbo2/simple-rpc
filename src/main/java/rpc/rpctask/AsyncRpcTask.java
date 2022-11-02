package rpc.rpctask;

import java.util.concurrent.Future;

import listener.ITaskFinishListener;
import lombok.Getter;
import lombok.Setter;

/**
 * 异步rpcTask
 * 
 * @author WinkeyZhao
 * @note
 *
 */
@Getter
@Setter
public class AsyncRpcTask<T> extends AbstractRpcTask<T> {
    // 异步rpcTask对消息回调处理handler绑定标识
    private int backBindId;
    // 异步rpcTask对消息回调处理发起者标识
    private long backExcuteId;
    // task完成逻辑处理监听
    private final ITaskFinishListener asyncJsonTaskFinishLisener;

    public AsyncRpcTask(int backBindId, long backExcuteId, String taskId,
	    ITaskFinishListener asyncJsonTaskFinishLisener,Class<T> clazz) {
	super(taskId,clazz);
	this.backBindId = backBindId;
	this.backExcuteId = backExcuteId;
	this.asyncJsonTaskFinishLisener = asyncJsonTaskFinishLisener;
    }

    @Override
    public void run() {
	try {
	    asyncJsonTaskFinishLisener.taskAsyncFinish(this);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void cancel() {
	System.out.println("AsyncRpcTask cancel");
    }
}
