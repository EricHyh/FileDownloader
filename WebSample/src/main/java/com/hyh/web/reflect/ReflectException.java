package com.hyh.web.reflect;

/**
 * @author Administrator
 * @description
 * @data 2018/11/16
 */

class ReflectException extends RuntimeException {

    ReflectException(String message) {
        super(message);
    }

    ReflectException(String message, Throwable cause) {
        super(message, cause);
    }
}
