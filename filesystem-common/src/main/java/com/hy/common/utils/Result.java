package com.hy.common.utils;

import java.util.HashMap;

/**
 * 操作消息提醒
 *
 * @author wyl
 * @since 2021-04-30 10:16:24
 */

public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private static final Integer SUCCESS_CODE = 200;
    private static final Integer ERR_CODE = 500;

    private static final String SUCCESS_TAG = "success";
    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "message";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "result";

    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息。
     */
    public Result() {
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public Result(boolean success, int code, String msg) {
        super.put(SUCCESS_TAG, success);
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public Result(boolean success, int code, String msg, Object data) {
        super.put(SUCCESS_TAG, success);
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(DATA_TAG, data);
    }

    /**
     * 方便链式调用
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static Result success() {
        return Result.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static Result success(Object data) {
        return Result.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static Result success(String msg) {
        return Result.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static Result success(String msg, Object data) {
        return new Result(true, SUCCESS_CODE, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static Result error() {
        return Result.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result error(String msg) {
        return Result.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result error(String msg, Object data) {
        return new Result(false, ERR_CODE, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static Result error(int code, String msg) {
        return new Result(false, code, msg, null);
    }
}

