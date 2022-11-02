package message.rpcReq;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
@Getter
@Setter
public class RpcCommonMsg extends AbstractMessage {

    public RpcCommonMsg(int bindId, Class<?> clazz) {
	super(bindId, clazz);
    }
}
