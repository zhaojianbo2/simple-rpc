package test.rpcserver;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public interface RpcActon {
    
    public void action(ChannelHandlerContext channelHandlerContext,JSONObject jsonObject);
}
