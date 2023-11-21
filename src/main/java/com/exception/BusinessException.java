package com.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @Description: 包装器业务异常类实现
 * @DATE: 2022/11/14  21:31
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    //默认设为400
    private int errCode= HttpStatus.BAD_REQUEST.value();

    private String errMsg;

    public BusinessException(int errCode) {
        this.errCode = errCode;
    }
    public BusinessException(String errMsg){
        this.errMsg = errMsg;
    }

//    public BusinessException(int errCode, String errMsg){
//        this.errCode = errCode;
//        this.errMsg = errMsg;
//    }

}