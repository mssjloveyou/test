package com.example.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExcectuerTest {
    static Boolean flag = true;
    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newCachedThreadPool();
//        Boolean flag = true;
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (flag){
                    System.out.println(this.getClass().getName() + " running");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10;i++){
                    System.out.println(this.getClass().getName() + " running");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                flag = false;
            }
        });
        threadPool.shutdown();
while (!threadPool.isTerminated()){
    try {
        System.out.println(threadPool.isTerminated());
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
        System.out.println("done");

//        threadPool.shutdown();



    }

}
