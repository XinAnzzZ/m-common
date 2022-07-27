package com.yuhangma.common.web.handler;

import com.yuhangma.common.core.exception.BizException;
import com.yuhangma.common.core.exception.UnauthorizedException;
import com.yuhangma.common.core.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/3/21
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult<Void> handleMethodArgumentNotValidException(HttpServletRequest req,
                                                                  MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        final String msg = result.getFieldErrors().stream().findFirst()
                .map(fieldError -> fieldError.getField() + fieldError.getDefaultMessage()).orElse("参数异常");
        return JsonResult.fail(msg);
    }

    @ExceptionHandler(Exception.class)
    public JsonResult<Void> handleGlobalException(HttpServletRequest req, Exception ex) {
        log.error(String.format("请求异常：%s", req.getRequestURI()), ex);
        Throwable cause = getCause(ex);
        if (isParameterIllegalException(cause)) {
            String exMessage = ex.getMessage();
            if (ex instanceof ConstraintViolationException) {
                // 校验异常默认会带上方法名，要去掉
                exMessage = ((ConstraintViolationException) ex).getConstraintViolations()
                        .stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
            }
            return JsonResult.fail(exMessage);
        }
        if (ex instanceof BizException) {
            return JsonResult.fail(ex.getMessage());
        }
        if (ex instanceof UnauthorizedException) {
            return JsonResult.fail(401, "登录已过期！");
        }
        return JsonResult.fail();
    }

    public static boolean isParameterIllegalException(Throwable error) {
        return error instanceof BindException || error instanceof MethodArgumentNotValidException ||
                error instanceof HttpRequestMethodNotSupportedException || error instanceof MissingServletRequestParameterException
                || error instanceof ConstraintViolationException || error instanceof IllegalArgumentException;
    }

    private Throwable getCause(Throwable error) {
        Throwable cause = error;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }
}

