package concurrent;

public class VolatileVisibilityTest {

    private static volatile boolean initFlag = false;

    public static void main(String[] args) throws InterruptedException {

        getThreadA().start();

        Thread.sleep(5000L);

        getThreadB().start();

        Thread.sleep(5000L);
    }

    public static Thread getThreadA(){
        return new Thread(() -> {
            System.out.println("Thread A start");
            while (!initFlag){

            }
            System.out.println("Thread A end");
        });
    }

    public static Thread getThreadB(){
        return new Thread(() -> {
            System.out.println("Thread B start");
            initFlag = true;
            System.out.println("Thread B end");
        });
    }
}
