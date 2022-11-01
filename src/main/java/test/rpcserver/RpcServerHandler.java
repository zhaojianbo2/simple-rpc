package test.rpcserver;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author : zGame
 * @version V1.0
 * @Project: simple-rpc
 * @Package test.rpcserver
 * @Description: TODO
 * @date Date : 2022年02月28日 17:55
 */
public class RpcServerHandler implements RpcActon{
    
    
    @Override
    public void action(ChannelHandlerContext channelHandlerContext,JSONObject jsonObject) {
        String taskId = jsonObject.getString("taskId");
        JSONObject message = jsonObject.getJSONObject("message");
        jsonObject.clear();
        jsonObject.put("taskId",taskId);
        jsonObject.put("result","this is serverResult_"+message.toJSONString());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channelHandlerContext.writeAndFlush(jsonObject.toJSONString());
    }
}
