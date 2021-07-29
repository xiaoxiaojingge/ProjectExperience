package com.itjing.webchat.idworker;

public interface RandomCodeStrategy {
    void init();

    int prefix();

    int next();

    void release();
}
