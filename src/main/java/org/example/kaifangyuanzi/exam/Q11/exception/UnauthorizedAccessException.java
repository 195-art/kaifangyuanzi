package org.example.kaifangyuanzi.exam.Q11.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("无权操作");
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
