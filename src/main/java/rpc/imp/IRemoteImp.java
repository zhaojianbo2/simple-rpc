package rpc.imp;

import java.util.concurrent.TimeUnit;

import listener.ITaskFinishListener;
import message.rpcReq.AbstractMessage;
import rpc.rpctask.AsyncRpcTask;
import rpc.rpctask.SyncRpcTask;

/**
 * 远程实现
 * 
 * @param taskMessageWrap
 * @param time
 * @param timeUnit
 * @return
 */
public interface IRemoteImp {

    public ITaskFinishListener getTaskFinishListener();

    public <T> T get(AbstractMessage msg, SyncRpcTask<T> syncRpcTask, long time, TimeUnit timeUnit);

    public <T> void runAsync(AbstractMessage msg, AsyncRpcTask<T> AsyncRpcTask,long time, TimeUnit timeUnit);
}
