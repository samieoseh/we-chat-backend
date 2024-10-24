package com.samuel.wechat.mappers;

public interface CreateMapper<A, B> {
    A mapFrom(B b);
}
