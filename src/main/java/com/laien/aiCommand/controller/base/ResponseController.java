package com.laien.aiCommand.controller.base;


/**
 *
 * rest 返回结果封装
 *
 * @date 2021-05-18
 * @author xushengdong
 */
public class ResponseController {

    /**
     * 成功返回
     *
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public ResponseResult<Void> succ() {
        return ResponseResult.succ();
    }

    /**
     * 成功返回
     *
     * @param data 数据
     * @return com.laien.workout.response.ResponseResult<T>
     */
    public <T> ResponseResult<T> succ(T data) {
        return ResponseResult.succ(data);
    }

    /**
     * 成功返回
     *
     * @param msg  描述
     * @param data 数据
     * @return com.laien.workout.response.ResponseResult<T>
     */
    public <T> ResponseResult<T> succ(String msg, T data) {
        return ResponseResult.succ(msg, data);
    }

    /**
     * 失败返回
     *
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public ResponseResult<Void> fail() {
        return ResponseResult.fail();
    }

    /**
     * 失败返回
     *
     * @param msg 描述
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public <T> ResponseResult<T> fail(String msg) {
        return ResponseResult.fail(msg);
    }

    /**
     * 失败返回
     *
     * @param code code码
     * @param msg  描述
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public <T> ResponseResult<T> fail(int code, String msg) {
        return ResponseResult.fail(code, msg);
    }

    /**
     * 失败返回
     *
     * @param responseCode 失败枚举
     * @return com.laien.workout.response.ResponseResult<java.lang.Void>
     */
    public <T> ResponseResult<T> fail(ResponseCode responseCode) {
        return ResponseResult.fail(responseCode.getCode(), responseCode.getMsg());
    }

}
