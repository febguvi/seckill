package com.gunjo.skilldemo.vo;

import com.gunjo.skilldemo.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;

/**
 * 登录参数
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
