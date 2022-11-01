package register;


import netty.NettyClient;


/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public interface IRpcConnectionRegister {
    
    public <T> void registerConnection(NettyClient<T> nettyClient);
    
}
