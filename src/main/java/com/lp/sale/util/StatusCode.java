package com.lp.sale.util;

public class StatusCode extends RuntimeException {
    private final int code;
    public StatusCode(int code, String message) {
        super(message);
        this.code = code;
    }
    public StatusCode(int code, String message, Throwable t) {
        super(message, t);
        this.code = code;
    }
    public int getCode() {
        return this.code;
    }
}