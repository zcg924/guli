package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author zcgstart
 * @create 2020-02-20 16:56
 */
@Data
@ApiModel(value = "全局异常对象")
public class GuliException extends RuntimeException{

    private Integer code;//错误码

    public GuliException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();

    }
}
