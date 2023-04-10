package concurrent;

/**
 * 线程的基础使用
 *
 * 中断方法
 * void interrupt()  中断线程,设置'中断标记位', 并不真的中断线程(需要自己写逻辑去中断线程)
 * boolean Thread.isInterrupted() 是否在'中断'状态
 * boolean Thread.interrupted() 是否在'中断'状态, 并清除'中断标记位'
 *
 * 守护线程: 当 java应用中只有 '守护线程', 则虚拟机会自然退出
 *
 * 线程优先级: 高优先级的线程在'竞争资源'时的概率较大而已, 并不是一定能竞争到.
 */
public class ThreadBaseUse {
    private static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup threadGroup = new ThreadGroup("threadGroup-01"); // 线程组
        Thread t1 = new Thread(threadGroup, new Thread1(obj), "thread-01");
        t1.start(); // 开启新线程;

        // Thread2 t2 = new Thread2();
        // t2.run(); // 不会开启新线程, 而是在当前线程中, 运行 run 中的内容;
        Thread t2 = new Thread(new Thread2(obj), "thread-02");
        t2.start();

        t1.setPriority(Thread.MIN_PRIORITY); // 低优先级
        t2.setPriority(Thread.MAX_PRIORITY); // 高优先级

        t1.interrupt();
        t2.interrupt();

        t1.join();
        t2.join();

        Thread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
    }
}

class Thread1 implements Runnable{
    private final Object obj;

    public Thread1(Object obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        System.out.println("hello! I'am thread-01, i implements Runnable interface.");
        while (true){
            // 自己实现 '中断'退出
            if(Thread.currentThread().isInterrupted()){
                System.out.println("thread-01 is interrupted, break.");
                break;
            }
            synchronized (obj){
                try {
                    System.out.println("thread-01 obj wait.");
                    obj.wait(); // 进入 'wait' 队列 (会释放 'obj的监视器', synchronize);
                    // Thread.sleep 不会释放 'obj的监视器'
                    System.out.println("thread-01 after wait.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Thread.yield();
        }
        System.out.println("thread-01 run end.");
    }
}

class Thread2 extends Thread{
    private final Object obj;

    public Thread2(Object obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        System.out.println("hello! I'am thread-02, i extends thread class.");
        while (true){
            synchronized (obj){
                System.out.println("thread-02 notify.");
                obj.notifyAll(); // 唤醒全部线程.
                obj.notify(); // 唤醒一个线程. (随机唤醒的, 不公平的)
            }
            // System.out.println("thread-02 run");
            Thread.yield();
        }
    }
}

class DaemonThread extends Thread{
    @Override
    public void run() {
        System.out.println("daemon-thread run.");
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
