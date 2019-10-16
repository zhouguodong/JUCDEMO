package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A线程打印5次   B线程打印10次    C 线程打印15次；依次打印
 * 来 10组
 * <p>
 * 可以锁定多个条件 condition
 */
class MyResource {
    private int num = 1;//A 1B 2C 3  thread
    Lock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();//因为是多条件的  一把锁 三个钥匙
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();

    public void print5() {
        lock.tryLock();
        try {
            //判断
            if (num != 1) {
                c1.await();
            }
            //干活
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            //通知
            num = 2;
            c2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10() {
        lock.tryLock();
        try {
            //判断
            if (num != 2) {
                c2.await();
            }
            //干活
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            //通知
            num = 3;
            c3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 倒回去 是个圆形的路子
     */
    public void print15() {
        lock.tryLock();
        try {
            //判断
            if (num != 3) {
                //c1.await() 看下效果？
                c3.await();
            }
            //干活
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            //通知
            num = 1;
            c1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}

public class Synchronized_LockDemo {
    public static void main(String[] args) {
        MyResource myResource = new MyResource();
        //开启三个线程
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                myResource.print5();
            }

        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                myResource.print10();
            }

        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                myResource.print15();
            }

        }, "C").start();


    }

}
