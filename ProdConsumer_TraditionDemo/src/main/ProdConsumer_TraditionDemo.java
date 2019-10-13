package main;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：一个初始值为0，一个线程+1，一个线程-1，交替五次。
 * 1. 线程  操作  资源类
 * 2. 判断  干活  通知
 * 3. 防止虚假唤醒机制
 *
 * 横幅：虚假唤醒机制
 * 上联：线程操作资源类 下联 ：判断 干活  通知
 *
 * 深、 透、 明、细
 */
class ShareDate{
    private int num=0;
    private Lock lock=new ReentrantLock();
    private Condition condition= lock.newCondition();


    public void increment() throws Exception{
        lock.lock();

        try {
            while(num!=0){  //理解一下生产者 和消费者 （一个增1，然后一个减1 就是一个得生产，一个得消费）
                //等待，不能生产蛋糕
               condition.await();
            }
            //干活
            num++;
            System.out.println(Thread.currentThread().getName()+"\t"+num);
            //唤醒
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception{
        lock.lock();

        try {
            while(num==0){  //理解一下生产者 和消费者 （一个增1，然后一个减1 就是一个得生产，一个得消费）
                //等待，不能吃蛋糕了
                condition.await();
            }
            //干活
            num--;
            System.out.println(Thread.currentThread().getName()+"\t"+num);
            //唤醒
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
public class ProdConsumer_TraditionDemo {

    public static void main(String[] args) {

       ShareDate shareDate=new ShareDate();
       new Thread(()->{
           try {
               for (int i = 0; i < 5; i++) {
                   shareDate.increment();
               }
           } catch (Exception e) {
               e.printStackTrace();
           }

       },"A").start();
        new Thread(()->{
            try {
              //  for (int i = 0; i < 5; i++) {
                    shareDate.decrement();
               // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"B").start();
    }
    //使用if判断 以上有隐患的 假设只有有四个就出现 连厨房都吃的现象


}
