package com.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;



@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("unused")
public class CommonResult<T> implements Serializable {

    private int code;

    private String message;

    private boolean isSuccess;

    private T data;

    public CommonResult() {
    }

    public CommonResult(int code, String message, boolean isSuccess) {
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public CommonResult(int code, String message, boolean isSuccess, T data) {
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public static <P> CommonResult<P> operateSuccess() {
        return UnmodifiableCommonResult.SUCCESS;
    }

    public static <P> CommonResult<P> operateSuccess(P p) {
        return new CommonResult<>(ResultConstant.SUCCESS_CODE, ResultConstant.OPERATE_SUCCESS_MESSAGE, true, p);
    }

    public static <P> CommonResult<P> operateFail(P p) {
        return new CommonResult<>(ResultConstant.FAIL_CODE, ResultConstant.OPERATE_FAIL_MESSAGE, false, p);
    }

    public static <P> CommonResult<P> operateFailWithMessage(String message) {
        return new CommonResult<>(ResultConstant.FAIL_CODE, message, false);
    }

    public static <P> CommonResult<P> operateNoPower(P p) {
        return new CommonResult<>(ResultConstant.FORBIDDEN_CODE, ResultConstant.OPERATE_NO_POWER, false, p);
    }

    @SuppressWarnings("unchecked")
    public static <P> CommonResult<P> autoResult(boolean isSuccess) {
        if (isSuccess) {
            return UnmodifiableCommonResult.SUCCESS;
        } else {
            return UnmodifiableCommonResult.FAILED;
        }
    }

    public static <P> CommonResult<P> autoResult(boolean isSuccess, P data) {
        if (isSuccess) {
            return CommonResult.operateSuccess(data);
        } else {
            return CommonResult.operateFail(data);
        }
    }

    @SuppressWarnings("unchecked")
    public CommonResult<T> operateFail() {
        return UnmodifiableCommonResult.FAILED;
    }

    @SuppressWarnings("unchecked")
    public CommonResult<T> operateNoPower() {
        return UnmodifiableCommonResult.NO_POWER;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @SuppressWarnings("rawtypes")
    private static class UnmodifiableCommonResult<P> extends CommonResult<P> {
        protected static final UnmodifiableCommonResult SUCCESS = new UnmodifiableCommonResult<>(ResultConstant.SUCCESS_CODE, ResultConstant.OPERATE_SUCCESS_MESSAGE, true);
        protected static final UnmodifiableCommonResult FAILED = new UnmodifiableCommonResult<>(ResultConstant.FAIL_CODE, ResultConstant.OPERATE_FAIL_MESSAGE, false);
        protected static final UnmodifiableCommonResult NO_POWER = new UnmodifiableCommonResult<>(ResultConstant.FORBIDDEN_CODE, ResultConstant.OPERATE_NO_POWER, false);

        public UnmodifiableCommonResult(int code, String message, boolean isSuccess) {
            super.setCode(code);
            super.setMessage(message);
            super.setSuccess(isSuccess);
        }

        @Override
        public CommonResult<P> setData(P data) {
            throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
        }

        @Override
        public CommonResult<P> setSuccess(boolean isSuccess) {
            throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
        }

        @Override
        public CommonResult<P> setMessage(String message) {
            throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
        }

        @Override
        public CommonResult<P> setCode(int code) {
            throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
        }
    }
}
