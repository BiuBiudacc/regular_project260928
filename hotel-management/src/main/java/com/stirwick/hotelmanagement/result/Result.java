package com.stirwick.hotelmanagement.result;
import  com.stirwick.hotelmanagement.enums.ResultCodeEnum;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 全局统一返回结果封装
 * @param <T> data泛型
 */
@Data
@Schema(description = "通用接口返回对象")
public class Result<T> {
    @Schema(description = "业务响应码，200代表成功")
    private Integer code;
    @Schema(description = "返回消息提示")
    private String msg;
    @Schema(description = "数据主体")
    private T data;

    //成功重载
    public static <T> Result<T> success(){
        return build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg(),null);
    }
    //成功自定义重载
    public static <T> Result<T> success(T data){
        return build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(),data);
    }
    //失败重载
    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum){
        return build(resultCodeEnum.getCode(),resultCodeEnum.getMsg(), null);
    }
    //失败重载,支持自定义消息
    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum,String msg){
        return build(resultCodeEnum.getCode(),msg,null);
    }
    //失败重载,完全自定义
    public static <T> Result<T> fail(Integer code,String msg){
        return build(code,msg,null);
    }
    private static <T> Result<T> build(Integer code,String msg, T data){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;

    }

}
