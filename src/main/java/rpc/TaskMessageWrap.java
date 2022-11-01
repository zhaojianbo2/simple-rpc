package rpc;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import message.IMessage;

/**
 * @author : zGame
 * @version V1.0
 * @Project: simple-rpc
 * @Package rpc
 * @Description: 对message进行包装,添加了taskId
 * @date Date : 2022年02月28日 13:28
 */
@Getter
public class TaskMessageWrap {
    
    private String taskId;
    
    private IMessage message;
    
    public TaskMessageWrap(String taskId, IMessage message) {
        this.taskId = taskId;
        this.message = message;
    }
    
    
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
