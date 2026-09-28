package com.stirwick.hotelmanagement.enums;
import lombok.Getter;
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "操作成功"),

    // =========业务错误 400xx =========
    PARAM_ERROR(40001, "请求参数非法"),
    USER_NOT_EXIST(40002, "用户不存在"),
    ROOM_NOT_EXIST(40003, "房间信息不存在"),


    // ========== 登录认证 401 ==========
    NOT_LOGIN(401,"未登录，请先登录"),
    TOKEN_INVALID(401,"token无效，请重新登录"),
    TOKEN_EXPIRED(401,"token已过期，请重新登录"),
    USER_PASSWORD_ERROR(400,"账号或密码错误"),
    USER_DISABLED(403,"账号已被禁用"),
    METHOD_NOT_ALLOWED(405,"请求方法不允许"),
    NOT_FOUND(404,"请求接口不存在"),
    SYSTEM_ERROR(500,"未知异常"),

    //业务错误
    BATCHINFO_CREATE_ERROR(5001,"批次创建失败"),
    BATCHinfo_CREATE_ERROR(5002,"批次修改失败");


    private final Integer code;
    private final String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
