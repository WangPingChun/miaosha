package com.imooc.seckill.result;

import java.util.Optional;

/**
 * @author : chris
 * 2018-07-25
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<>(codeMsg);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg codeMsg) {
        final Optional<CodeMsg> msgOptional = Optional.ofNullable(codeMsg);
        msgOptional.ifPresent((cm) -> {
                    this.code = cm.getCode();
                    this.msg = cm.getMsg();
                }
        );
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
