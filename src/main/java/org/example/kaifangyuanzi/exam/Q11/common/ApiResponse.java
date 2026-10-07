package org.example.kaifangyuanzi.exam.Q11.common;

import lombok.Data;

/**
 * 统一API响应结果
 */
@Data
public class ApiResponse<T> {
    /** 状态码 */
    private int code;
    /** 提示信息 */
    private String message;
    /** 响应数据 */
    private T data;

    /**
     * 构造成功响应
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * 构造失败响应
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
