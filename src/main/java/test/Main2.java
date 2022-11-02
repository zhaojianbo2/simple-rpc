package test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 
 * @author WinkeyZhao
 * @note
 *
 */
public class Main2 {
    
    public static void main(String[] args) {
        
        CompletableFuture<Integer> future = new CompletableFuture<Integer>();
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.complete(1);
        }).start();
        try {
            System.out.println("---" + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
