package concurrent;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 广义上来讲, 是对 '锁' 的扩展
 * synchronize 和 ReentrantLock 都是只允许一个线程访问一个资源
 * 信号量可以指定多个;
 */
public class SemaphoreTest {
    public static Semaphore semaphore = new Semaphore(5, false);

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            getThread(i + "").start();
        }
    }

    public static Thread getThread(String name){
        return new Thread(() -> {
            try {
                semaphore.acquire();
                Thread.sleep(1000);
                System.out.println("read thread : " + name + " done!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        });
    }
}