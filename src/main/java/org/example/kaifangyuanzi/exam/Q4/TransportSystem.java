package org.example.kaifangyuanzi.exam.Q4;

interface GPSLocatable{
    String getLocation();
}

abstract class TransportVehicle{
    protected String vehicleId;

    public TransportVehicle(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    abstract double calculateCost(double weight);
}

class Truck extends TransportVehicle implements GPSLocatable{
    public Truck(String vehicleId) {
        super(vehicleId);
    }

    @Override
    public double calculateCost(double weight) {
        return weight * 5.0;
    }

    @Override
    public String getLocation() {
        return "仓库A区";
    }
}

class Drone extends TransportVehicle implements GPSLocatable{
    public Drone(String vehicleId) {
        super(vehicleId);
    }

    @Override
    public double calculateCost(double weight) {
        return weight * 15.0;
    }

    @Override
    public String getLocation() {
        return "高空坐标(39.9, 116.4)";
    }
}

public class TransportSystem {
    public static void main(String[] args) {
        TransportVehicle truck = new Truck("T-998");
        TransportVehicle drone = new Drone("D-007");
        double cargoWeight = 10.0;

        // 输出卡车信息
        System.out.printf("卡车[%s] 当前位置：%s | 配送%.0fkg货物费用：%.1f元%n",
                truck.vehicleId,
                ((GPSLocatable) truck).getLocation(),
                cargoWeight,
                truck.calculateCost(cargoWeight));

        // 输出无人机信息
        System.out.printf("无人机[%s] 当前位置：%s | 配送%.0fkg货物费用：%.1f元%n",
                drone.vehicleId,
                ((GPSLocatable) drone).getLocation(),
                cargoWeight,
                drone.calculateCost(cargoWeight));
    }
}
