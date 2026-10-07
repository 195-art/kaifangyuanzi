package org.example.kaifangyuanzi.exam.Q11.exception;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
        super("操作失败");
    }

    public BusinessException(String message) {
        super(message);
    }
}
