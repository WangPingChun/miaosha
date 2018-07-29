package com.imooc.seckill.exception;

import com.imooc.seckill.result.CodeMsg;

/**
 * @author : chris
 * 2018-07-28
 */
public class GlobalException extends RuntimeException {
    private CodeMsg codeMsg;
    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
