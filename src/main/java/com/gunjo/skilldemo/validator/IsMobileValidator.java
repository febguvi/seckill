package com.gunjo.skilldemo.validator;

import com.gunjo.skilldemo.utils.ValidatorUtil;
import org.thymeleaf.util.*;

import javax.validation.*;

/**
 * 手机号码校验规则
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        // 手机号码必填则校验
        if (required) {
            return ValidatorUtil.isMobile(value);
        } else {  // 非必填若空则返回，不为空也要校验
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }

    }


}
