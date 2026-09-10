package org.example.kaifangyuanzi.exam.Q8;

public class MilkTeaShopTest {
    public static void main(String[] args) {
        BarCounter barCounter = new BarCounter();

        Thread clerk1 = new Thread(new Clerk(barCounter, "店员张三"));
        Thread clerk2 = new Thread(new Clerk(barCounter, "店员李四"));

        Thread customer1 = new Thread(new Customer(barCounter, "顾客王五"));
        Thread customer2 = new Thread(new Customer(barCounter, "顾客赵六"));

        clerk1.start();
        clerk2.start();
        customer1.start();
        customer2.start();
    }


}
