package message.rpcReq;

import lombok.Getter;
import lombok.Setter;
import message.IMessage;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
@Getter
@Setter
public class PlayerMessage implements IMessage {
    private int messageId = 1001;
    private String msgData = "PlayerMessage_Content";

}
