package com.wht.blog.exception;

/**
 * Tip 提示异常
 *
 * @author wht
 * @since 2019-09-15 21:44
 */
public class TipException extends RuntimeException{

    public TipException() {
    }

    public TipException(String message) {
        super(message);
    }

    public TipException(String message, Throwable cause) {
        super(message, cause);
    }

    public TipException(Throwable cause) {
        super(cause);
    }
}
