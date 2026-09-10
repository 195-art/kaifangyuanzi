package org.example.kaifangyuanzi.exam.Q8;

import java.util.concurrent.BlockingQueue;

public class Customer implements Runnable{
    private final BarCounter barCounter;
    private final String name;
    private static final int TOTAL_DRINK = 10;

    public Customer(BarCounter barCounter, String name) {
        this.barCounter = barCounter;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < TOTAL_DRINK; i++) {
            try {
                barCounter.take(name);
                Thread.sleep((long) (Math.random() * 800));

            }catch (InterruptedException e) {
                e.printStackTrace();
                throw  new RuntimeException(e);
            }
        }
        System.out.println("===== " + name + " 喝够10杯奶茶，满足离开了 =====");
    }
}
