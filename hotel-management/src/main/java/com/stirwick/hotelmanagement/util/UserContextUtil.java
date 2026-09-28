package com.stirwick.hotelmanagement.util;

public class UserContextUtil {
    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    public static void setUserId(Long userId){
        USER_ID_HOLDER.set(userId);
    }
    public static Long getUserId(){
        return USER_ID_HOLDER.get();
    }

    /**
     * 清空线程数据，请求结束一定要执行！！
     */
    public static void clear(){
        USER_ID_HOLDER.remove();
    }
}
