package org.example.kaifangyuanzi.exam.Q11.exception;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException() {
        super("活动不存在");
    }

    public EventNotFoundException(String message) {
        super(message);
    }


}
