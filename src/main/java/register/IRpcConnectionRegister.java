package register;


import netty.NettyClient;


/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public interface IRpcConnectionRegister {
    
    public void registerConnection(NettyClient nettyClient);
    
}
