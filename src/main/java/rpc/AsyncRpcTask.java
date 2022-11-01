package rpc;

import java.util.concurrent.Executor;

import lombok.Getter;

@Getter
public class AsyncRpcTask<T> extends RpcTask<T> {
    // 异步rpcTask对消息回调处理handler绑定标识
    private int msgId;
    // 执行线程
    private Executor excutor;

    public AsyncRpcTask(int msgId, Executor excutor, String taskId) {
	super(taskId);
	this.msgId = msgId;
	this.excutor = excutor;
    }

}
