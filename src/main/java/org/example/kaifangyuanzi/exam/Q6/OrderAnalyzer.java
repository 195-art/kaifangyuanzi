package org.example.kaifangyuanzi.exam.Q6;

import java.util.*;

public class OrderAnalyzer {
    private List<Order> orders = new ArrayList<>();

    public void add(Order order){
        orders.add(order);
    }

    public void removeInvalidOrders(){
        // 修正：使用 removeIf 按条件删除，底层是迭代器实现，不会触发并发修改异常
        orders.removeIf(order -> order.getAmount() <= 0);
    }

    public Set<String> getAllUniqueTags(){
        Set<String> uniqueTags = new HashSet<>();
        for(Order order:orders){
            uniqueTags.addAll(order.getTags());
        }
        return uniqueTags;
    }

    public Map<String,Double> getCustomerSpendMap(){
        Map<String,Double> spendMap = new HashMap<>();
        for(Order order:orders){
            String customerName = order.getCustomerName();
            spendMap.merge(customerName, order.getAmount(), Double::sum);
        }
        return spendMap;
    }

    public List<Order> sortOrdersByCustomerName(){
        List<Order> sortedList = new ArrayList<>(orders);
        sortedList.sort(Comparator.comparing(Order::getCustomerName));
        return sortedList;
    }

    public static void main(String[] args) {
        OrderAnalyzer analyzer = new OrderAnalyzer();

        analyzer.add(new Order("O001", "Alice", 150.0, Arrays.asList("电子", "数码")));
        analyzer.add(new Order("O002", "Bob", -50.0, Arrays.asList("服装")));
        analyzer.add(new Order("O003", "Alice", 200.0, Arrays.asList("图书", "电子")));
        analyzer.add(new Order("O004", "Charlie", 200.0, Arrays.asList("生鲜")));
        analyzer.add(new Order("O005", "Bob", 300.0, Arrays.asList("服装", "户外")));

        analyzer.orders.forEach(System.out::println);

        analyzer.removeInvalidOrders();
        analyzer.orders.forEach(System.out::println);

        System.out.println(analyzer.getAllUniqueTags());

        analyzer.getCustomerSpendMap().forEach((name, total) -> System.out.printf("%s: %.1f元%n", name, total));

        analyzer.sortOrdersByCustomerName().forEach(System.out::println);

        List<Order> defaultSorted = new ArrayList<>(analyzer.orders);
        Collections.sort(defaultSorted);
        defaultSorted.forEach(System.out::println);
    }
}
