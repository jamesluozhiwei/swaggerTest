package com.lzw.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author James
 * 统一API响应类
 */
@ApiModel
public class ApiResponseObject {

    @ApiModelProperty(value = "响应标识")
    private int code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private Object data;

    public ApiResponseObject() {
        this.code = 0;
        this.msg = "test";
        this.data = null;
    }

    public ApiResponseObject(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public ApiResponseObject setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ApiResponseObject setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ApiResponseObject setData(Object data) {
        this.data = data;
        return this;
    }
}
