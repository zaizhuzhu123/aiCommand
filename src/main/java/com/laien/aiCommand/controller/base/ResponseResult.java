package com.laien.aiCommand.controller.base;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 返回结果封装
 *
 * @author xushengdong
 */
@ApiModel(value="返回结果", description="返回结果")
public final class ResponseResult<T> {

    @ApiModelProperty(value = "code码，200成功，500异常...其他" , required = true)
    private int code;
    @ApiModelProperty(value = "描述信息", required = true)
    private String message;
    @ApiModelProperty(value = "业务数据", required = true)
    private T data;

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    /**
     * 成功返回
     *
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public static ResponseResult<Void> succ() {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg());
    }

    /**
     * 成功返回
     *
     * @param data 数据
     * @return com.laien.workout.response.ResponseResult<T>
     */
    public static <T> ResponseResult<T> succ(T data) {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), data);
    }

    /**
     * 成功返回
     *
     * @param msg  描述
     * @param data 数据
     * @return com.laien.workout.response.ResponseResult<T>
     */
    public static <T> ResponseResult<T> succ(String msg, T data) {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 失败返回
     *
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public static ResponseResult<Void> fail() {
        return new ResponseResult<>(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMsg());
    }

    /**
     * 失败返回
     *
     * @param msg 描述
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public static <T> ResponseResult<T> fail(String msg) {
        return new ResponseResult<>(ResponseCode.FAILURE.getCode(), msg);
    }

    /**
     * 失败返回
     *
     * @param code code码
     * @param msg  描述
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public static <T> ResponseResult<T> fail(int code, String msg) {
        return new ResponseResult<>(code, msg);
    }

    /**
     * 失败返回
     *
     * @param responseCode 失败枚举
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public static <T> ResponseResult<T> fail(ResponseCode responseCode) {
        return new ResponseResult<>(responseCode.getCode(), responseCode.getMsg());
    }
}
