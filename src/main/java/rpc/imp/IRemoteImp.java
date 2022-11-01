package rpc.imp;


import listener.ITaskFinishListener;
import rpc.TaskMessageWrap;

/**
 * 远程实现
 * 
 * @param taskMessageWrap
 * @param time
 * @param timeUnit
 * @return
 */
public interface IRemoteImp<T> {

    public ITaskFinishListener<T> getTaskFinishListener();

    public void sendMsg(TaskMessageWrap taskMsg);
}
