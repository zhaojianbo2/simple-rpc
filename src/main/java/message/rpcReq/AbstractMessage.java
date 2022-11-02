package message.rpcReq;

import java.util.UUID;

import lombok.Getter;
import message.IMessage;

@Getter
public class AbstractMessage implements IMessage {
    protected final int bindId;
    protected final Class<?> clazz;

    public AbstractMessage(int bindId, Class<?> clazz) {
	this.bindId = bindId;
	this.clazz = clazz;
    }

    @Override
    public String getTaskId() {
	return UUID.randomUUID().toString();
    }
}
