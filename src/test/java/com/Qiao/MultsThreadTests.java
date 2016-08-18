package com.Qiao;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by white and black on 2016/8/13.
 */
class Consumer implements Runnable{
    private BlockingQueue<String> q;
    public Consumer(BlockingQueue<String> q){
        this.q=q;
    }
    @Override
    public void run() {
        try{
            while(true){
                Thread.sleep(20000);
                System.out.println(Thread.currentThread().getName()+":"+q.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class Producer implements Runnable{
    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> queue) {
        this.q=queue;
    }


    @Override
    public void run() {
        try{
            for(int i=0;i<100;i++){
                Thread.sleep(100);
                q.put(String.valueOf(i));
                System.out.println(Thread.currentThread().getName());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class MyThread extends Thread{
    private int tid;
    MyThread(int tid){
        this.tid=tid;
    }
    @Override
    public void run() {
        try{
            for(int i=0;i<10;i++){
                System.out.println(String.format("%d:%d",tid,i));
                Thread.sleep(1000);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
public class MultsThreadTests {
    public static void testThread(){
        for(int i=0;i<10;i++){
            new MyThread(i).start();
        }
    }
    private static MultsThreadTests multsThreadTests1=new MultsThreadTests();
    private static MultsThreadTests multsThreadTests2=new MultsThreadTests();


    public static void synchronized1(){
        synchronized(multsThreadTests1){
            try{
                for(int i=0;i<50;i++){
                    Thread.sleep(100);
                    System.out.println("T1");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    public static void synchronized2(){
        synchronized(multsThreadTests1){
            try{
                for(int i=0;i<50;i++){
                    Thread.sleep(100);
                    System.out.println("T2");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void testSynchronized(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized1();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized2();
            }
        }).start();
    }

    private static ThreadLocal<Integer> threadLocalUserId=new ThreadLocal<Integer>();
    private static int UserId;

    public static void testThreadLocal(){
        for(int i=0;i<5;++i) {
            final int a = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserId.set(a);
                    try{

                        Thread.sleep(100);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //System.out.printf("threadUserId:%d",threadLocalUserId.get());
                    System.out.println(String.format("threadUserId:%d",threadLocalUserId.get()));
                }
            }).start();
        }
    }

    public static void testThreadLocal1(){
        for(int i = 0; i< 10 ; ++i){
            final int FinalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        UserId = FinalI;
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName()+": "+UserId);
                    }catch (Exception e){
                        e.printStackTrace();                    }
                }
            }).start();
        }
    }

    public static void testThreadLocal2(){

        for(int i=0;i<=5;i++){
            final int a=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{

                        Thread.sleep(100);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //System.out.printf("threadUserId:%d",threadLocalUserId.get());
                    System.out.println(String.format("threadUserId:%d",a));
                }
            }).start();
        }
    }


    public static  void testBlockingQueue(){
        BlockingQueue<String> queue;
        queue = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(queue),"Producer").start();
        new Thread(new Consumer(queue),"Consumer1").start();
        new Thread(new Consumer(queue),"Consumer2").start();
    }
    private  static AtomicInteger atomicInteger=new AtomicInteger();

    public static void testExecutor(){
//        ExecutorService service= Executors.newSingleThreadExecutor();
        atomicInteger.set(100);
        ExecutorService service=Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
//                for(int i=0;i<10;i++){
//                    try{
//                        Thread.sleep(100);
//                        System.out.println("Executor1:"+i);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
                while(atomicInteger.get()>0){
                    try{
                        Thread.sleep(100);

                        System.out.println("Executor1: "+atomicInteger.decrementAndGet());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
//                for(int i=0;i<10;i++){
//                    try{
//                        Thread.sleep(100);
//                        System.out.println("Executor2:"+i);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//                while(atomicInteger.get()>0){
                while(atomicInteger.get()>0){
                    try{
                        Thread.sleep(100);

                        System.out.println("Executor2: "+atomicInteger.decrementAndGet());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        service.shutdown();
        while(!service.isTerminated()){
//        while(atomicInteger.get()>0){
            try{
                Thread.sleep(1000);
                System.out.println("wait for termination.");
//                service.shutdownNow();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static  AtomicInteger a=new AtomicInteger();
    private static int count;
    public static void testWithAtomic(){
        count=100;
        a.set(100);
        for(int i=0;i<10;++i){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(100);
                        for(int j=0;j<10;j++){
                            count--;
                            System.out.println("Atomic:"+a.decrementAndGet());
                            System.out.println("count:"+count);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static void testFuture(){
        ExecutorService service=Executors.newSingleThreadExecutor();
        Future<Integer> future=service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                //return null;
                Thread.sleep(10000);
                return  1;
            }
        });
        service.shutdown();
        try{
            System.out.println(future.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){

//        testThread();
//        testSynchronized();
//        testThreadLocal();
//        testThreadLocal1();
//        testThreadLocal2();
//        testBlockingQueue();
//        testExecutor();
//        testWithAtomic();
        testFuture();
    }
}
