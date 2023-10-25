package com.result;



public class ResultConstant {

    public static final String OPERATE_SUCCESS_MESSAGE = "success";
    public static final String OPERATE_FAIL_MESSAGE = "fail";
    public static final String OPERATE_NO_POWER = "no power";

    /**
     * 请求成功
     */
    public static final int SUCCESS_CODE = 1;

    /**
     * 请求失败
     */
    public static final int FAIL_CODE = 0;

    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 12;

    /**
     * 用户信息不存在
     */
    public static final int USER_NULL = 401;

    /**
     * 用户信息已经存在
     */
    public static final int USER_EXIST = 402;

    /**
     * 无权访问
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * 无效的UUID
     */
    public static final int API_INVALID_UUID = 405;

    /**
     * 参数不合法
     */
    public static final int PARAMETER_VALIDATION_ERROR = 406;


    /**
     * 用户还未登录或登录态已失效
     */
    public static final int USER_NOT_LOGIN = 407;

    /**
     * 密码错误
     */
    public static final int USER_VERIFY_FAILED = 408;

    /**
     * 用户被禁用
     */
    public static final int USER_LIMITED = 409;

    /**
     * 缺少必要参数 sRedirectUrl
     */
    public static final int REDIRECT_URL_NULL = 415;

    /**
     * 与 sType 对应的回调地址未记录
     */
    public static final int CALLBACK_URL_NULL = 416;

    /**
     * 授权过期或者已经结束
     */
    public static final int AUTHORIZATION_OVER = 417;

    /**
     * 缺少 CSRF 防御参数
     */
    public static final int STATE_NULL = 422;

    /**
     * 错误的重定向方法，只支持GET和POST
     */
    public static final int METHOD_ERROR = 423;

    /**
     * 查询指定id应用详情为空
     */
    public static final int APP_PARAM_NULL = 431;

    /**
     * 查询指定id日志详情为空
     */
    public static final int LOG_PARAM_NULL = 431;

    /**
     * 查询指定id文档详情为空
     */
    public static final int MD_PARAM_NULL = 431;

}
