package concurrent;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author carson_luo
 */
public class Foo {

    private final Semaphore s1 = new Semaphore(0);
    private final ReentrantLock lock2 = new ReentrantLock();

    public Foo() {
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();

    }

    public void third(Runnable printThird) throws InterruptedException {
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }

    public static void main(String[] args) throws InterruptedException{
        Foo f = new Foo();
        int[] arr = new int[]{3,2,1};
        for (int i : arr) {
            if (i == 1){
                new Thread(() -> {
                    try {
                        f.first(() -> System.out.println("first"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            if (i == 2){
                new Thread(() -> {
                    try {
                        f.second(() -> System.out.println("second"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            if (i == 3){
                new Thread(() -> {
                    try {
                        f.third(() -> System.out.println("third"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
