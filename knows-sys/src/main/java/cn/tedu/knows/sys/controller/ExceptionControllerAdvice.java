package cn.tedu.knows.sys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 这个注解表示当前控制器方法运行到特殊时间节点时
// 可以运行额外代码的方法,我们这里只考虑控制器方法发生异常时运行的代码
//             ↓↓↓↓↓↓
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    // 下面编写一个注解,表示该方法是专门处理控制器发生的异常
    @ExceptionHandler
    public String hanldeException(Exception e){
        log.error("发生其他异常",e);
        return e.getMessage();
    }




}
