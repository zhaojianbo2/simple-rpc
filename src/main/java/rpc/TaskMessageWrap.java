package rpc;

import com.alibaba.fastjson.JSON;
/**
 * 
 */
import lombok.Getter;
import message.IMessage;
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
