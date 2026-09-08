package org.example.kaifangyuanzi.exam.Q4;

public class MyArrayList <T>{
    private Object[] elements;
    private int size;

    public MyArrayList(){
        elements = new Object[3];
        size = 0;
    }

    public void add(T element){
        if(size == elements.length){
            int oldCapacity = elements.length;
            int newCapacity = oldCapacity * 2;
            Object[] newElements = new Object[newCapacity];
            for(int i = 0; i < oldCapacity; i++){
                newElements[i] = elements[i];
            }
            elements = newElements;
            System.out.println("触发底层扩容！当前数组容量由 " + oldCapacity + " 扩容至 " + newCapacity);
        }
        elements[size] = element;
        size++;
        System.out.println("成功添加：" + element);
    }

    public T get(int index){
        if(index >= size || index < 0){
            return null;
        }
        return (T)elements[index];
    }

    public void remove(int index){
        if(index >= size || index < 0){
            return;
        }
        T removed =  (T)elements[index];
        for(int i = index; i < size - 1; i++){
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        System.out.println("删除了索引[" + index + "]的元素(" + removed + ")");
    }

    public static void main(String[] args) {
        MyArrayList<String> myArrayList = new MyArrayList<>();

        myArrayList.add("单号A");
        myArrayList.add("单号B");
        myArrayList.add("单号C");
        myArrayList.add("单号D");
        myArrayList.add("单号E");

        System.out.println("获取索引[2]的元素：" + myArrayList.get(2));

        myArrayList.remove(1);

        System.out.println("验证移位，此时索引[1]的元素变为了：" + myArrayList.get(1));
    }
}
