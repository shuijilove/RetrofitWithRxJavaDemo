package com.zhj.retrofitwithrxjavademo.util;

/**
 * 自定义异常
 * Created by hjzhang on 2016/7/26.
 */
public class APIException extends RuntimeException{
    public int code;
    public String message;

    public APIException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
