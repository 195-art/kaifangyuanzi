package org.example.kaifangyuanzi.exam.Q2;

public class ShopDemo {
    public static void main(String[] args) {
        //声明并初始化商品变量
        String goodsName = "机械键盘";
        double price = 150.00;
        int stock = 100;
        boolean isPromotion = false;
        int buyCount = 2;
        double totalPrice = price * buyCount;

        //判断是否包邮及字符串打印
        boolean freeShipping = isPromotion || totalPrice >= 100;
        System.out.println("商品：" + goodsName + "，单价：" + price + "元，促销中：" + isPromotion +
                "，购买" + buyCount + "件，总价为：" + totalPrice + "元");
        System.out.println("是否享受包邮：" + freeShipping);

        //交换变量
        int a = 10,b = 20;
        System.out.println("交换前: a=" + a + ",b=" + b);
        a = a^b;
        b = a^b;
        a = a^b;
        System.out.println("交换后: a=" + a + ",b=" + b);
//        int c = 10,d = 20;
//        c = c+d;
//        d = c-d;
//        c = c-d;

    }
}
