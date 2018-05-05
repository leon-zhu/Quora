package com.quora;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/4 19:51
 * @version: 1.0
 */


class MyThread extends Thread {

    private int tid;

    MyThread(int tid) {
        this.tid = tid;
    }


    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000); //当前线程放弃cpu的使用时间1秒
                System.out.println(String.format("%d:%d", tid, i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class MultiThreadTests {

    public static void testThread() {
        for (int i = 0; i < 10; i++) {
            //new MyThread(i).start();
        }

        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            TimeUnit.MILLISECONDS.sleep(1000); //当前线程放弃cpu的使用时间1秒
                            System.out.println(String.format("T2-%d:%d", threadId, i));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static Object obj = new Object(); //synchronized锁住对象

    public static void testSynchronized1() {
        synchronized (obj) {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    System.out.println(String.format("T3-%d", i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (obj) {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    System.out.println(String.format("T4-%d", i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void testSynchronized() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testBlockingQueue() {
        BlockingQueue<String> bq = new LinkedBlockingDeque<>();
        new Thread(new Producer(bq)).start();
        new Thread(new Consumer(bq), "consumer_1").start();
        new Thread(new Consumer(bq), "consumer_2").start();

    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadLocalUserIds.set(finalI);
                        userId = finalI;
                        TimeUnit.MILLISECONDS.sleep(1000);
                        System.out.println("ThreadLocal: " + threadLocalUserIds.get());
                        System.out.println("userId: " + userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testExecutor() {
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        System.out.println("Executor1: " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        System.out.println("Executor2: " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //service.shutdown(); //任务执行完毕, 关闭
        while (!service.isTerminated()) {
            try {
//                TimeUnit.MILLISECONDS.sleep(1000);
//                service.shutdown();
//                service.awaitTermination(10,TimeUnit.MILLISECONDS);
//                service.shutdownNow();
                System.out.println("Wait for termination...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void testWithoutAtomic() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        for (int j = 0; j < 10; j++)
                            counter++;
                        System.out.println(counter);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testWithAtomic() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        for (int j = 0; j < 10; j++) {
                            atomicInteger.incrementAndGet();
                            System.out.println(atomicInteger);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    public static void testFuture() {
        //ExecutorService service = Executors.newFixedThreadPool(2);
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
        service.shutdown();
        try {
            System.out.println(future.get(1000, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            System.out.println("等待超时");
            //e.printStackTrace();
        }

    }


    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        //testThread();
        //testSynchronized();
        //testBlockingQueue();
        //testThreadLocal();
        //testExecutor();
        //testWithAtomic();
        testFuture();
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> bq;

    public Consumer(BlockingQueue<String> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(Thread.currentThread().getName() + ": " + bq.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Producer implements Runnable {

    private BlockingQueue<String> bq;

    public Producer(BlockingQueue<String> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
                bq.put(String.valueOf(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
