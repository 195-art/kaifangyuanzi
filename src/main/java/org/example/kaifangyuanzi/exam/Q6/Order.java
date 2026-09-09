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
        if(Double.compare(this.amount,o.amount)==0){
            return Double.compare(o.amount,this.amount);
        }else {
            return this.orderId.compareTo(o.orderId);
        }
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
