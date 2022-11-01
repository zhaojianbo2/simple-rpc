package test.rpcserver;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author : zGame
 * @version V1.0
 * @Project: simple-rpc
 * @Package test.rpcserver
 * @Description: TODO
 * @date Date : 2022年02月28日 17:56
 */
public interface RpcActon {
    
    public void action(ChannelHandlerContext channelHandlerContext,JSONObject jsonObject);
}
