package com.laien.aiCommand.controller.base;

/**
 * @author xushengdong
 */

public enum ResponseCode {
    /**
     * 成功处理
     */
    SUCCESS(200, "SUCCESS"),
    /**
     * 失败处理
     */
    FAILURE(500, "FAILURE"),
    /**
     * 未登录
     */
    NON_LOGIN(1000, "You are not logged in"),
    /**
     * 登录失效
     */
    LOGIN_EXPIRED(1001, "The authentication session has expired. Please sign-in again"),
    /**
     * 没有权限
     */
    NO_PERMISSION(1002, "No permission"),
    /**
     * 参数错误
     */
    PARAM_ERROR(2000,"Parameter error");
    /**
     * code码
     */
    private final int code;
    /**
     * 描述信息
     */
    private final String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
