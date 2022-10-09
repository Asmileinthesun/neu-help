package com.hzx.search.thred;

import java.util.concurrent.*;

public class ThredTest {
    public static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("-------------ssss");
        CompletableFuture<Integer> runAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.err.println("i = " + i);
            return i;
        }, service).whenComplete((res,u)-> System.out.println("111"+res));
        System.out.println("eeee-------------");
    }

    public static void therd(String[] args) {
        System.out.println("-------------ssss");
        /**
         *th1 thread = new th1();
         *         thread.start();
         * th2 th2 = new th2();
         *         new Thread(th2).start();
         *         FutureTask<Integer> task = new FutureTask<>(new th3());
         *         new Thread(task).start();
         */
        service.execute(new th2());
        /**
         *
         */
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
        System.out.println("eeee-------------");
    }

    public static class th1 extends Thread {
        @Override
        public void run() {
            System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.err.println("i = " + i);
        }
    }

    public static class th2 implements Runnable {

        @Override
        public void run() {
            System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.err.println("i = " + i);
        }
    }

    public static class th3 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.err.println("i = " + i);
            return i;
        }
    }
}
