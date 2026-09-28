package com.stirwick.hotelmanagement.exception;
import com.stirwick.hotelmanagement.enums.ResultCodeEnum;
import com.stirwick.hotelmanagement.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * 全局异常捕获处理器
 * @RestControllerAdvice：拦截所有@RestController控制器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e){
        // log.warn：业务可控异常，warn级别，打印code和提示消息，不打印完整堆栈
        log.warn("业务异常 code={}, msg={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e){
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : ResultCodeEnum.PARAM_ERROR.getMsg();
        log.warn("参数校验异常：{}", msg);
        return Result.fail(ResultCodeEnum.PARAM_ERROR, msg);
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e){
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : ResultCodeEnum.PARAM_ERROR.getMsg();
        log.warn("参数绑定异常：{}", msg);
        return Result.fail(ResultCodeEnum.PARAM_ERROR, msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("JSON请求体解析失败:{}", e.getMessage());
        return Result.fail(ResultCodeEnum.PARAM_ERROR, "请求JSON格式错误或字段类型不匹配");
    }
    /**
     * 【兜底捕获】所有其他未知异常：空指针、数据库异常、数组越界等
     * Exception.class：最大父类，所有异常都能捕获
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        // log.error(描述, e)：第二个参数传入异常对象，打印完整异常堆栈！！
        // 非常关键：服务器日志保存堆栈，用于我们排查bug，但是堆栈不返回前端
        log.error("系统未知异常", e);
        // 前端只返回友好提示，不暴露堆栈信息（安全）
        return Result.fail(ResultCodeEnum.SYSTEM_ERROR);
    }
}
