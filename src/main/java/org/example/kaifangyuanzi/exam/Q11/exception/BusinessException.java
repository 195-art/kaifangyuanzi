package org.example.kaifangyuanzi.exam.Q11.exception;

public class BusinessException extends RuntimeException {

    public BusinessException() {
        super("操作失败");
    }

    public BusinessException(String message) {
        super(message);
    }
}
