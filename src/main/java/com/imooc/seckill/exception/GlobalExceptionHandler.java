package com.imooc.seckill.exception;

import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author : chris
 * 2018-07-27
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        if (e instanceof GlobalException) {
            GlobalException globalException = (GlobalException) e;
            final CodeMsg codeMsg = globalException.getCodeMsg();
            return Result.error(codeMsg);
        } else if (e instanceof BindException) {
            BindException exception = (BindException) e;
            final List<ObjectError> allErrors = exception.getAllErrors();
            final ObjectError objectError = allErrors.get(0);
            final String defaultMessage = objectError.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(defaultMessage));
        } else {
            log.error(e.getCause().getMessage());
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
