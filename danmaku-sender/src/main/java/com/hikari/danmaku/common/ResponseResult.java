package com.hikari.danmaku.common;

import com.hikari.danmaku.constants.ResultCode;

public class ResponseResult<T> {

    public int code; //返回状态码200成功

    private String msg; //返回描述信息

    private T data; //返回内容体

    public ResponseResult<T> setCode(ResultCode retCode) {
        this.code = retCode.code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ResponseResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }


}
