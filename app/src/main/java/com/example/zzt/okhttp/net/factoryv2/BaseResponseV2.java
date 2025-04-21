package com.example.zzt.okhttp.net.factoryv2;


import java.io.Serializable;

public class BaseResponseV2<T> implements Serializable {
    public static final String CODE_SUCCESS = "0";
    public static final String CODE_ERROR = "1";

    private String errorCode;
    private String errorMsg;
    public T data; //数据

    public BaseResponseV2(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseResponseV2(String errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}