package rpc.asyncHandler;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public interface BaseHandler<T> {

    public void action(T data);
}
