package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zcgstart
 * @create 2020-02-14 9:26
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 所有controller层抛出的异常都会在此捕获
     * 捕获Exception异常和Exception子类异常
     * @param e
     * @return 返回R对象的json形式
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        //打印异常跟踪栈，在控制台打印异常信息
        //e.printStackTrace();
        log.error(ExceptionUtils.getMessage(e));
        //返回异常结果的R对象
        return  R.error();
    }


    /**
     * 捕获特定异常对象BadSqlGrammarException
     * @param e
     * @return
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e){
        //e.printStackTrace();
        log.error(ResultCodeEnum.BAD_SQL_GRAMMAR.toString());
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e){
        //e.printStackTrace();
        log.error(ResultCodeEnum.JSON_PARSE_ERROR.toString());
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }




    //自定义异常处理
    //使用一个异常处理方法处理所有异常信息，并展示个性化异常结果
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        //e.printStackTrace();
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
