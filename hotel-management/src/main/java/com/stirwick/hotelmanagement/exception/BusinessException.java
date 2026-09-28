package com.stirwick.hotelmanagement.exception;
import com.stirwick.hotelmanagement.enums.ResultCodeEnum;
import lombok.Getter;
@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    public BusinessException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMsg());
        this.code = resultCodeEnum.getCode();
    }

    public BusinessException(ResultCodeEnum resultCodeEnum,String msg){
        super(msg);
        this.code = resultCodeEnum.getCode();
    }

    public BusinessException(Integer code,String msg){
        super(msg);
        this.code = code;
    }

}
