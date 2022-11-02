package rpc.rpctask;

import lombok.Getter;
import lombok.Setter;

//rpcTask抽象类
@Getter
@Setter
public abstract class AbstractRpcTask<T> implements Runnable {

    // rpcTask唯一标识
    private final String taskId;
    protected Class<T> clazz;
    // 返回data
    protected T returnData;

    public AbstractRpcTask(String taskId,Class<T> clazz) {
	this.taskId = taskId;
	this.clazz = clazz;
    }
    @SuppressWarnings("unchecked")
    public void setReturnData(Object data) {
	this.returnData = (T) data;
    }
    public abstract void cancel();
}
