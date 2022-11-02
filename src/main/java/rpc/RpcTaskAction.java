package rpc;

import message.rpcReq.AbstractMessage;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public interface RpcTaskAction {
    public <T> T get(AbstractMessage message, long time, TimeUnit timeUnit);

    public <T> void runAsync(AbstractMessage message, int backBindId, long backExcuteId, long time, TimeUnit timeUnit);
}
