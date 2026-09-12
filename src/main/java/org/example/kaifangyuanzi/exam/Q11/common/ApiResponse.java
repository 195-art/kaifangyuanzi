package org.example.kaifangyuanzi.exam.Q11.common;

import lombok.Data;

@Data
public class ApiResponse <T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse r = new ApiResponse<Object>();
        r.setCode(200);
        r.setData(data);
        r.setMessage("success");
        return r;
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse r = new ApiResponse<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}
