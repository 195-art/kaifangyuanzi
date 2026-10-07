package org.example.kaifangyuanzi.exam.Q11.exception;

/**
 * 无权限访问异常
 */
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("无权操作");
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
