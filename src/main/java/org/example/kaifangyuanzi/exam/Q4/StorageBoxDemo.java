package org.example.kaifangyuanzi.exam.Q4;

class StorageBox<T>{
    private T item;

    public void storeItem(T item){
        this.item = item;
    }

    public T retrieveItem(){
        return item;
    }
}

public class StorageBoxDemo {
    public static void main(String[] args) {
        StorageBox<Product> productStorageBox = new StorageBox<>();
        productStorageBox.storeItem(new Product("P001", "机械键盘", 450.0));
        Product retrievedProduct = productStorageBox.retrieveItem();
        System.out.println("📦商品储物箱存取测试：成功取出商品 -> " + retrievedProduct.getName());

        StorageBox<TransportVehicle> vehicleStorageBox = new StorageBox<>();
        vehicleStorageBox.storeItem(new Drone("D-007"));
        TransportVehicle retrievedVehicle = vehicleStorageBox.retrieveItem();
        System.out.println("📦设备储物箱存取测试：成功取出设备 -> 无人机 [" + retrievedVehicle.vehicleId + "]");
    }
}
