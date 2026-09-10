package org.example.kaifangyuanzi.exam.Q8;

public class Clerk implements Runnable{
    private final BarCounter barCounter;
    private final String name;
    private static final int TOTAL_MAKE = 10;

    public Clerk(BarCounter barCounter, String name) {
        this.barCounter = barCounter;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < TOTAL_MAKE; i++) {
            try {

                barCounter.put(name);
                Thread.sleep((long) (Math.random() * 500));

            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        System.out.println("===== " + name + " 做完10杯奶茶，下班了 =====");
    }
}
