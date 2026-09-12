package org.example.kaifangyuanzi.exam.Q11.exception;

import org.example.kaifangyuanzi.exam.Q11.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice = 全局的"兜底网"：不管哪个接口抛异常，先到这里过一遍
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 活动不存在 → 返回 404 + ApiResponse 格式
    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleEventNotFoundException(EventNotFoundException e) {
        return ApiResponse.error(404, e.getMessage());
    }

    // 无权操作 → 返回 403 + ApiResponse 格式
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleUnauthorized(UnauthorizedAccessException e) {
        return ApiResponse.error(403, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusiness(BusinessException e) {
        return ApiResponse.error(400, e.getMessage());

    }
}