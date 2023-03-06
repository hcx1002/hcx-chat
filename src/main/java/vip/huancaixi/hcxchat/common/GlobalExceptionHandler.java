package vip.huancaixi.hcxchat.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.huancaixi.hcxchat.utils.Respond;

/**
 * 全局异常处理器
 *
 * @author xuyuxiang
 * @date 2021/10/9 14:59
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 不同异常返回不同结果
     **/
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Respond handleException(Exception e) {
        return GlobalExceptionUtil.getCommonResult(e);
    }
}
