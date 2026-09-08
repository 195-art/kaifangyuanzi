package org.example.kaifangyuanzi.exam.Q4;

class LogisticsConfig{
    private String systemName = "顺风智能仓储系统";
    private static final LogisticsConfig INSTANCE = new LogisticsConfig();

    private LogisticsConfig() {}

    public static LogisticsConfig getInstance(){
        return INSTANCE;
    }
    public String getSystemName() {
        return systemName;
    }
}

class VehicleFactory{
    public static TransportVehicle createVehicle(String type){
        if(type.equals("Truck")){
            return new Truck("T-998");
        }else if(type.equals("Drone")){
            return new  Drone("D-007");
        }
        return null;
    }

}

class LogisticsOrder{
    private String orderId;
    private String sender;      // 寄件人
    private String receiver;    // 收件人
    private boolean isFragile;  // 是否易碎品
    private double insuredAmount;

    private LogisticsOrder(Builder builder){
        this.orderId = builder.orderId;
        this.sender = builder.sender;
        this.receiver = builder.receiver;
        this.isFragile = builder.isFragile;
        this.insuredAmount = builder.insuredAmount;
    }

    public static class Builder{
        private final String orderId;
        private String sender;      // 寄件人
        private String receiver;    // 收件人
        private boolean isFragile;  // 是否易碎品
        private double insuredAmount;

        public Builder(String orderId) {
            this.orderId = orderId;
        }

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder receiver(String receiver) {
            this.receiver = receiver;
            return this;
        }

        public Builder isFragile(boolean isFragile) {
            this.isFragile = isFragile;
            return this;
        }

        public Builder insuredAmount(double insuredAmount) {
            this.insuredAmount = insuredAmount;
            return this;
        }

        public LogisticsOrder build(){
            return new LogisticsOrder(this);
        }
    }
    @Override
    public String toString() {
        return String.format("订单号[%s], 寄件人[%s], 收件人[%s], 易碎品[%s], 保价[%.1f元]",
                orderId, sender, receiver, isFragile, insuredAmount);
    }
}

public class DesignPatternDemo {
    public static void main(String[] args) {
        LogisticsConfig configA = LogisticsConfig.getInstance();
        LogisticsConfig configB = LogisticsConfig.getInstance();
        System.out.println("单例测试：配置类实例A与实例B是否相同？ -> " + (configA == configB));

        TransportVehicle drone = VehicleFactory.createVehicle("Drone");
        System.out.println("工厂调度：工厂成功分配 -> 🚁无人机 [" + drone.vehicleId + "]，计费启动...");

        LogisticsOrder order = new LogisticsOrder.Builder("OD-999")
                .sender("Alice")
                .receiver("Bob")
                .isFragile(true)
                .insuredAmount(1000.0)
                .build();
        System.out.println("运单生成：" + order);
    }
}
