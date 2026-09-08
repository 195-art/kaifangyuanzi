package org.example.kaifangyuanzi.exam.Q4;

public class Product {
    private String id;
    private String name;
    private double price;

    private static int totalProductCount = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //无参构造
    public Product() {
        totalProductCount++;
    }

    //全参构造
    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        totalProductCount++;
    }

    public static int getTotalCount() {
        return totalProductCount;
    }

    public void showProductInfo() {
        System.out.println("商品信息: [" + id + "] " + name + ", 价格: " + price + "元");
    }

    public static void main(String[] args) {
        Product product1 = new Product("P001", "机械键盘", 450.0);
        Product product2 = new Product("P002","蓝牙耳机",299.0);

        product1.showProductInfo();
        product2.showProductInfo();

        System.out.println("系统当前共创建了 " + Product.getTotalCount() + " 个商品实例。");
    }
}
