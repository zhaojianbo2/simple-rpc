package rpc.asyncHandler;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public interface AsyncBaseHandler<T> {

    public void action(T data);
}
