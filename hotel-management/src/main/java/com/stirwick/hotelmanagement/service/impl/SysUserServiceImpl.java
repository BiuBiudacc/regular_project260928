package com.stirwick.hotelmanagement.service.impl;
import com.stirwick.hotelmanagement.dto.LoginDTO;
import com.stirwick.hotelmanagement.entity.SysUser;
import com.stirwick.hotelmanagement.enums.ResultCodeEnum;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.mapper.SysUserMapper;
import com.stirwick.hotelmanagement.service.SysUserService;
import com.stirwick.hotelmanagement.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDTO loginDTO) {
        // 1. 根据账号查询用户
        SysUser user = sysUserMapper.selectByUsername(loginDTO.getUsername());
        if(user == null){
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_ERROR);
        }

        // 2. 判断账号是否禁用
        if(user.getEnabled() == 0){
            throw new BusinessException(ResultCodeEnum.USER_DISABLED);
        }

        // 3. 密码比对用法不变，接口方法 matches(明文,数据库密文)
        boolean matchOk = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        if(!matchOk){
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_ERROR);
        }
        // 4. 全部校验通过，生成JWT token返回
        return jwtUtil.generateToken(user.getId());
    }
}
