package com.imooc.seckill.validator;

import com.imooc.seckill.util.ValidatorUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : chris
 * 2018-07-27
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
    private boolean require = false;

    @Override
    public void initialize(Mobile constraintAnnotation) {
        require = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (require) {
            return ValidatorUtils.mobile(value);
        }
        return true;
    }
}
