package base.web.common;

import constants.StatusCode;
import dto.ResultDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller的公共异常处理类
 */
//@ControllerAdvice//开启了对Controller增强的功能，使用当前类增强（AOP）
//@ResponseBody
//组合注解，相当于@ControllerAdvice+@ResponseBody
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler//只要有异常，则会调用该方法处理
    //@ExceptionHandler(Throwable.class)//可以指定抓取具体异常
    //@ResponseBody
    public ResultDTO errorHandle(Throwable e){
        //记录日志（发邮件、发短信、、、、）
        System.out.println("记录日志：发生了异常");
        e.printStackTrace();
        //返回结果
        //return new ResultDTO(false, StatusCode.ERROR,"操作失败");
        return new ResultDTO(false, StatusCode.ERROR,e.getMessage());
    }
    
}
