package com.stirwick.hotelmanagement.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stirwick.hotelmanagement.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("select * from sys_user where username = #{username} and deleted = 0")
    public SysUser selectByUsername(@Param("username") String username);
}
