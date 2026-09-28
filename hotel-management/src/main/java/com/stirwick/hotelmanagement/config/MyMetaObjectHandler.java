package com.stirwick.hotelmanagement.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.stirwick.hotelmanagement.util.UserContextUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * insert 执行时触发
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // strictInsertFill：如果业务代码已经手动设置过该字段值，则不覆盖，更加安全
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // 从ThreadLocal拿到当前登录用户ID
        this.strictInsertFill(metaObject, "createUser", UserContextUtil::getUserId, Long.class);
        this.strictInsertFill(metaObject, "updateUser", UserContextUtil::getUserId, Long.class);
    }

    /**
     * update执行时触发
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, "updateUser", UserContextUtil::getUserId, Long.class);
    }
}
