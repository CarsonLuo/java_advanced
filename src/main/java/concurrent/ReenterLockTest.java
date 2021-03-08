package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁
 * 当 '释放锁的次数' > '加锁的次数', 抛异常 java.lang.IllegalMonitorStateException
 * lock() 加锁
 * unlock() 释放锁
 * lockInterrupted() 加锁, 可相应 '中断'
 * tryLock(5, TimeUnit.SECONDS) 加锁, 等待时间
 *
 * 相对于 'synchronize', 用 ReentrantLock 灵活一点
 */
public class ReenterLockTest {
    public static ReentrantLock lock = new ReentrantLock();
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    public static ReentrantLock fairLock = new ReentrantLock(true); // 公平锁

    public static Condition condition = lock.newCondition();

    public static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = getInterruptedDemo(lock1, lock2);
        Thread t2 = getInterruptedDemo(lock2, lock1);
        t1.start();
        t2.start();

        t2.interrupt();

        t1.join();
        t2.join();
        System.out.println(count);

        Thread conditionThread = getConditionDemo();
        conditionThread.start();

        Thread.sleep(1000);

        lock.lock(); // 调用 condition.signal() 需要先获取锁, 不然会抛异常
        condition.signal(); // 通知 conditionThread 继续执行;
        lock.unlock();
    }

    public static Thread getThread(){
        return new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lock.lock();
                try{
                    count++;
                }finally {
                    lock.unlock();
                }
            }
        });
    }

    public static Thread getInterruptedDemo(ReentrantLock l1, ReentrantLock l2){
        return new Thread(() -> {
            try {
                l1.lockInterruptibly();
                Thread.sleep(500);
                l2.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " run success.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if(l1.isHeldByCurrentThread()){
                    l1.unlock();
                }
                if(l2.isHeldByCurrentThread()){
                    l2.unlock();
                }
                System.out.println(Thread.currentThread().getName() + " exit.");
            }
        });
    }

    public static Thread getConditionDemo(){
        return new Thread(() -> {
            lock.lock();
            try {
                condition.await(); // 会释放相应的锁; 被其他线程唤醒后, 还是要获取与之绑定的重入锁;
                System.out.println("condition-thread go on.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        });
    }
}
