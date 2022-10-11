package com.hzx.member.exception;

public class UsernameException extends RuntimeException {
    public UsernameException() {
        super("用户名存在");
    }
}
