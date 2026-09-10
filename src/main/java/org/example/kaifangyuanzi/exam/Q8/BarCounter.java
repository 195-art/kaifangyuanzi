package org.example.kaifangyuanzi.exam.Q8;

public class BarCounter {
    private static final int MAX_SIZE = 5;
    private int currentCount = 0;

    public synchronized void put(String clerkName){
        try {
            while(currentCount >= MAX_SIZE){
                System.out.println("吧台已满，" + clerkName + "等待中...");
                wait();
            }
            currentCount++;
            System.out.println("[" + clerkName + "]制作了[奶茶]，吧台当前数量：" + currentCount);

            notifyAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public synchronized void take(String customerName){
        try {
            while(currentCount <= 0){
                System.out.println("吧台为空，" + customerName + "等待中...");
                wait();
            }
            currentCount--;
            System.out.println("[" + customerName + "]取走了[奶茶]，吧台剩余数量：" + currentCount);

            notifyAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
