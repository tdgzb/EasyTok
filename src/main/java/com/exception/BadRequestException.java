package com.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @Description:
 * @DATE: 2023/11/6  21:02
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Getter
public class BadRequestException extends RuntimeException{

    private Integer status = HttpStatus.BAD_REQUEST.value();

    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(HttpStatus status,String msg){
        super(msg);
        this.status = status.value();
    }


}
