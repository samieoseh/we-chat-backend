package com.samuel.wechat.exceptions;

import javax.naming.AuthenticationException;

public class InvalidUsernameAndPasswordException extends AuthenticationException {

    public InvalidUsernameAndPasswordException(String msg) {
        super(msg);
    }
}
