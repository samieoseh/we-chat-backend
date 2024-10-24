package com.samuel.wechat.mappers;

public interface ReadMapper<A, B> {
    B mapTo(A a);
}
