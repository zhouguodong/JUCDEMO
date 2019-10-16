package mian;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 * 串：
 * volatile/CAS/AtomicInteger/BlockQueue
 * 图
 * ture
 * --->(生产者)
 * <---(消费者)
 *
 * --->
 * <---
 *
 * false
 *
 * 保证线程的可见性（volatile）
 * 自增（AtomicIntege）
 * 架构代码：统配 适用 通用 传接口不传具体的类
 */
class MySource{
    private volatile boolean flag=true; //可见性
    private AtomicInteger atomicInteger=new AtomicInteger();
    //架构代码：统配 适用 通用 传接口不传具体的类
    //不 new 子类 目的就是 通用  Spring 的依赖注入 1构造注入，2 set注入
    BlockingQueue<String> blockingQueue=null;

    public MySource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        //反射 获取类名
        System.out.println(blockingQueue.getClass().getName());
    }
    public void myProd() throws Exception {
        while(flag){
            //工作中适用 offer
            String s = atomicInteger.incrementAndGet() + "";
            boolean offerResult = blockingQueue.offer(s, 2l, TimeUnit.SECONDS);
            if(offerResult){
                System.out.println(Thread.currentThread().getName()+"\t"+"插入队列成功,元素："+s);
            }else{
                System.out.println(Thread.currentThread().getName()+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"boss stop, flag==false");
    }

    public void MyConsumer()throws Exception{
        while(flag){
            String poll = blockingQueue.poll(2l, TimeUnit.SECONDS);
            if(poll==null||"".equals(poll)){
                flag=false;
                System.out.println(Thread.currentThread().getName()+"");
            }
            System.out.println(Thread.currentThread().getName()+"\t"+"消费消息"+poll+"成功");

        }
    }
    public void stop(){
        flag=false;
    };
}

public class ProdConsumer_BlockingDemo {
    public static void main(String[] args) {
        MySource mySource=new MySource(new ArrayBlockingQueue<>(10));

        new Thread(()->{
            try {
                mySource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }

        },"prod").start();

     new Thread(()->{
        try {
            mySource.MyConsumer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    },"prod").start();

        try {
            TimeUnit.SECONDS.sleep(5l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("5秒时间到，结束");
        mySource.stop();
    }
}



