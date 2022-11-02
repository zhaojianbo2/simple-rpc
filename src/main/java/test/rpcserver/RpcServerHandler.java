package test.rpcserver;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
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
