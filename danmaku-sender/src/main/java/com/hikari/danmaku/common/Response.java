package com.hikari.danmaku.common;

import com.hikari.danmaku.constants.ResultCode;

public class Response {

    private final static String SUCCESS = "success";

    private final static String FAIL = "fail";

    public static <T> ResponseResult<T> makeOKRsp() {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS).setMsg(SUCCESS);
    }

    public static <T> ResponseResult<T> makeOKRsp(String message) {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS).setMsg(message);
    }

    public static <T> ResponseResult<T> makeOKRsp(T data) {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS).setMsg(SUCCESS).setData(data);
    }

    public static <T> ResponseResult<T> makeErrRsp(String message) {
        return new ResponseResult<T>().setCode(ResultCode.INTERNAL_SERVER_ERROR).setMsg(message);
    }

    public static <T> ResponseResult<T> makeRsp(int code, String msg) {
        return new ResponseResult<T>().setCode(code).setMsg(msg);
    }

    public static <T> ResponseResult<T> makeRsp(int code, String msg, T data) {
        return new ResponseResult<T>().setCode(code).setMsg(msg).setData(data);
    }
}