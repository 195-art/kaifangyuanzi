package org.example.kaifangyuanzi.exam.Q5;

class InsufficientFundsException  extends Exception {
    private final String transactionId;

    public InsufficientFundsException(String message,String transactionId) {
        super(message);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}

class SystemMaintenanceException  extends RuntimeException {
    public SystemMaintenanceException(String message,Throwable cause) {
        super(message,cause);
    }
}

class PaymentProcessor{
    public void pay(double amount,String transactionId) throws InsufficientFundsException{
        if(amount<0){
            throw new IllegalArgumentException("支付金额不能为负数");
        }
        if(amount>10000){
            throw new InsufficientFundsException("余额不足",transactionId);
        }
        if(amount==500){
            throw new SystemMaintenanceException("网络连接超时", new NullPointerException("底层网络连接对象为空"));
        }
        System.out.printf("交易[%s]支付成功，金额：%.1f%n", transactionId, amount);
    }

}

class PaymentService{
    public boolean processPayment(double amount, String transactionId){
        PaymentProcessor processor = new PaymentProcessor();
        try {
            processor.pay(amount,transactionId);
            return true;
        }catch (InsufficientFundsException e){
            System.out.println("余额不足，流水号为" + e.getTransactionId());
            return false;
        }catch (IllegalArgumentException e){
            throw new SystemMaintenanceException("系统校验失败",e);
        }catch (Exception e){
            System.out.println("发生未知异常:" + e.getMessage());
            return false;
        }finally {
            System.out.println("执行资源清理任务...");
        }
    }
}

public class ExceptionDemo {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();

        System.out.println("测试场景1：-10");
        try {
            paymentService.processPayment(-10, "TX001");
        } catch (SystemMaintenanceException e) {
            System.out.println("上层捕获系统异常：" + e.getMessage());
        }

        System.out.println("\n测试场景2：20000");
        paymentService.processPayment(20000, "TX002");

        System.out.println("\n测试场景3：500");
        paymentService.processPayment(500, "TX003");

        System.out.println("\n测试场景4：800(正常)");
        boolean payResult = paymentService.processPayment(800, "TX004");
        System.out.println("本次支付结果：" + payResult);
    }
}
