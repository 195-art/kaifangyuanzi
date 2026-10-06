package org.example.kaifangyuanzi.exam.Q6;

import java.util.List;

public class Order implements Comparable<Order>{
    private String orderId;
    private String customerName;
    private double amount;
    private List<String> tags;

    public Order(String orderId, String customerName, double amount, List<String> tags) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
        this.tags = tags;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    public List<String> getTags() {
        return tags;
    }


    @Override
    public int compareTo(Order o) {
        int result = Double.compare(o.amount, this.amount);
        return result != 0 ? result : this.orderId.compareTo(o.orderId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                ", tags=" + tags +
                '}';
    }

}
