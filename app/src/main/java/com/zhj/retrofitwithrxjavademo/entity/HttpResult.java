package com.zhj.retrofitwithrxjavademo.entity;

import com.zhj.retrofitwithrxjavademo.util.Constant;

public class HttpResult<T> {

    public int code;
    public String desc;
    public T content;


    public boolean isSuccess() {
        return code == Constant.SUCCESS;
    }

    public boolean isEmpty() {
        return code == Constant.EMPTY;
    }

    public boolean isNoMore() {
        return code == Constant.NOMORE;
    }
}
