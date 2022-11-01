package rpc;

import message.IMessage;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public interface RpcTaskAction {
    public <T> T get(IMessage message, long time, TimeUnit timeUnit);

    public <T> void getAsync(IMessage message);
}
