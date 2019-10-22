package main;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThradPoll {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(2,5,5l, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
        //threadPoolExecutor.execute();
    }
}
