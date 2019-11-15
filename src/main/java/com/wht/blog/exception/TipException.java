package com.wht.blog.exception;

import com.wht.blog.util.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Tip 提示异常
 *
 * @author wht
 * @since 2019-09-15 21:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TipException extends RuntimeException{

    private Integer code;
    private String message;

    public TipException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }
    public TipException(String message) {
        super(message);
        this.message = message;
    }

    public TipException(String message, Throwable cause) {
        super(message, cause);
    }

    public TipException(Throwable cause) {
        super(cause);
    }
}
